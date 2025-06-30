package domain;

import LogicStructures.LogicQueue;
import LogicStructures.LogicRoadList;
import LogicStructures.LogicTrafficLightList;
import LogicStructures.LogicVerticesList;
import Nodes.Node;
import Nodes.NodeE;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Nodes.NodeTrafficLight;
import Structures.Graph;
import Structures.RoadList;
import Structures.TrafficLightList;
import Structures.VerticesList;
import business.CarManager;

import java.util.Random;

/*
 * Representa un auto que recorre el grafo. Al implementar Runnable puede
 * ejecutarse en un hilo independiente y avanzar por las calles siguiendo la
 * ruta calculada.
 */
public class Car implements Runnable {

	private static int counter = 0;
	private static final int VELOCITY_STANDARD = 1000;
	private final int id;
	private NodeV origin;
	private NodeV destination;
    private CarManager controller;
    private int lastRow = -1;
    private int lastCol = -1;

    /*
     * Crea un auto con un origen y destino específicos. El manager controla las
     * actualizaciones de posición sobre el grid.
     */
    public Car(NodeV origin, NodeV destination, CarManager controller) {
        this.id = ++counter;
        this.origin = origin;
        this.destination = destination;
        this.controller = controller;
    }

    /*
     * Hilo principal del auto. Calcula su ruta, avanza por las calles y
     * recalcula si encuentra una vía bloqueada.
     */
    @Override
    public void run() {
		Graph g = GraphRoad.getGraph();

		while (true) {
			int[] path = Dijkstra.buildPath(origin.getData(), destination.getData(), g);
			boolean recalcRoute = false;

			System.out.print("Ruta Dijkstra: ");
			for (int i = 0; i < path.length; i++) {
				System.out.print(path[i]);
				if (i < path.length - 1)
					System.out.print(" -> ");
			}
			System.out.println();

			for (int i = 0; i < path.length; i++) {
				NodeV node = findNode(path[i], g);
				if (node == null)
					continue;

				System.out.println(this + " -> " + toCoord(node.getData()));
				LogicQueue.add(this, node.getCars());

				int totalDelay = 0;
				RoadList rList = null;
				NodeV next = null;

				if (i < path.length - 1) {
					next = findNode(path[i + 1], g);
					NodeE edge = findEdge(node, next);
					if (edge != null) {
						totalDelay = (int) edge.getWeight() * VELOCITY_STANDARD;
					}
					rList = selectRoadList(node, next);
				}
				if (rList != null && !LogicRoadList.isEmpty(rList)) {
					int steps = LogicRoadList.size(rList) + 1;
					int stepDelay = (steps > 0) ? totalDelay / steps : totalDelay;
					NodeRoad cursor = rList.getFirst();

					while (cursor != null) {
						if (isRoadBlocked(cursor)) {
							System.out.println("Calle bloqueada en (" + cursor.getI() + "," + cursor.getJ()
									+ ") para Car " + id + ". Recalculando ruta...");

							// Interrumpimos el camino actual
							origin = node; // nodo actual
							LogicQueue.pop(node.getCars());
							recalcRoute = true;
							break;
						}

						if (controller != null) {
							controller.updateCarPosition(lastRow, lastCol, cursor.getI(), cursor.getJ(), this);
							lastRow = cursor.getI();
							lastCol = cursor.getJ();
						}

						try {
							Thread.sleep(stepDelay);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							return;
						}

						System.out.println(this + " -> (" + cursor.getI() + "," + cursor.getJ() + ")");
						cursor = cursor.getNext();
					}

					if (recalcRoute) {
						break;
					}

					if (next != null && controller != null) {
						waitForGreenLight(node, next); // espera semáforo
						int nrow = next.getData() / 1000;
						int ncol = next.getData() % 1000;
						controller.updateCarPosition(lastRow, lastCol, nrow, ncol, this);
						lastRow = nrow;
						lastCol = ncol;
						next.setOcupado(false); // liberar intersección

					}

					try {
						Thread.sleep(stepDelay);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}

				} else {
					if (i < path.length - 1 && next != null && controller != null) {
						waitForGreenLight(node, next); // espera semáforo
						int nrow = next.getData() / 1000;
						int ncol = next.getData() % 1000;
						controller.updateCarPosition(lastRow, lastCol, nrow, ncol, this);
						lastRow = nrow;
						lastCol = ncol;
						next.setOcupado(false);

					}

					try {
						Thread.sleep(totalDelay);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}

				if (!recalcRoute) {
					LogicQueue.pop(node.getCars());
				}
			}
			if (recalcRoute) {
				continue;
			}

			System.out.println("Ruta terminada.");

			origin = destination;
			destination = getRandomDestination(origin, g);
		}
	}

    /*
     * Consulta al manager si la celda indicada está bloqueada por un incidente
     * o mantenimiento.
     */
    private boolean isRoadBlocked(NodeRoad r) {
        if (controller == null)
            return false;
        return controller.isBlocked(r.getI(), r.getJ());
    }

    /*
     * Espera pasivamente hasta que el semáforo permita el paso hacia el nodo
     * destino y la intersección esté libre.
     */
    private void waitForGreenLight(NodeV from, NodeV to) {
        while (true) {
            if (canPass(from, to) && !to.isOcupado()) {
                to.setOcupado(true); // Entrar a la intersección
                break;
            }

            try {
                Thread.sleep(100); // espera pasiva
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


        /*
         * Verifica el estado de los semáforos del nodo destino para saber si el
         * auto puede continuar su trayectoria.
         */
        private boolean canPass(NodeV from, NodeV to) {
	    int fromRow = from.getData() / 1000;
	    int fromCol = from.getData() % 1000;
	    int toRow = to.getData() / 1000;
	    int toCol = to.getData() % 1000;

	    TrafficLightList lights = to.getTrafficLights();
	    if (lights == null) return false;

	    NodeTrafficLight xLight = LogicTrafficLightList.findByDirection(lights, "X");
	    NodeTrafficLight yLight = LogicTrafficLightList.findByDirection(lights, "Y");

	    if (fromRow == toRow)
	        return xLight != null && xLight.isGreen(); // E-O u O-E
	    if (fromCol == toCol)
	        return yLight != null && yLight.isGreen(); // N-S o S-N

	    return false;
	}


        /*
         * Devuelve la lista de calles que conectan los dos nodos indicados.
         */
        private RoadList selectRoadList(NodeV originV, NodeV destinationV) {
		int oRow = originV.getData() / 1000;
		int oCol = originV.getData() % 1000;
		int dRow = destinationV.getData() / 1000;
		int dCol = destinationV.getData() % 1000;

		if (oRow == dRow)
			return originV.getxRoads();
		if (oCol == dCol)
			return originV.getyRoads();
		return null;
	}

        /*
         * Busca y devuelve el nodo del grafo cuyo identificador coincide con el
         * valor proporcionado.
         */
        private NodeV findNode(int data, Graph g) {
		if (g.getVertices() == null)
			return null;
		NodeVertex curr = g.getVertices().getFirst();
		while (curr != null) {
			if (curr.getNodeV().getData() == data)
				return curr.getNodeV();
			curr = curr.getNext();
		}
		return null;
	}

        /*
         * Devuelve la arista que conecta los nodos dados o null si no existe.
         */
        private NodeE findEdge(NodeV origin, NodeV destination) {
                if (origin == null || origin.getEdges() == null)
                        return null;
                Node current = origin.getEdges().getFirst();
                while (current != null) {
                        NodeE e = current.getNodeE();
                        if (e.getDestination() == destination)
                                return e;
                        current = current.getNext();
                }
                return null;
        }

        /*
         * Identificador único del auto.
         */
        public int getId() {
                return id;
        }

        /*
         * Vértice actual donde se encuentra el auto.
         */
        public NodeV getOrigin() {
                return origin;
        }

        /*
         * Vértice objetivo al que se dirige.
         */
        public NodeV getDestination() {
                return destination;
        }

        /*
         * Representación textual sencilla para depuración.
         */
        @Override
        public String toString() {
                return "Car " + id;
        }

        /*
         * Convierte el identificador numérico en coordenadas de fila y columna.
         */
        private static String toCoord(int id) {
                int row = id / 1000;
                int col = id % 1000;
                return "(" + row + "," + col + ")";
        }

        /*
         * Escoge un nuevo destino aleatorio distinto al actual para que el auto
         * continúe circulando por el grafo.
         */
        private NodeV getRandomDestination(NodeV current, Graph g) {
		VerticesList vList = g.getVertices();
		if (vList == null)
			return current;

		int size = LogicVerticesList.size(vList);
		if (size <= 1)
			return current;

		Random rand = new Random();
		NodeV dest = current;
		while (dest == current) {
			int index = rand.nextInt(size);
			NodeVertex node = vList.getFirst();
			for (int i = 0; i < index && node != null; i++) {
				node = node.getNext();
			}
			if (node != null)
				dest = node.getNodeV();
		}
		return dest;
	}
}