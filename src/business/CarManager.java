package business;

import java.util.Random;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import LogicStructures.LogicQueue;
import LogicStructures.LogicVerticesList;
import Structures.Graph;
import Structures.RoadList;
import Structures.VerticesList;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import domain.Car;
import domain.GraphRoad;
import domain.Incident;

public class CarManager {
	private GridPane grid;
	/*
	 * Valor por defecto para quitarse de encima algun desborde.
	 */
	private Car[][] gridCarPositions = new Car[20][20];
	private boolean[][] blockedRoad = new boolean[20][20];
	private MainController controller;

	public CarManager(MainController controller) {
		this.controller = controller;
	}

	public void initGrid(GridPane grid, int gridSize) {
		this.grid = grid;
		gridCarPositions = new Car[gridSize][gridSize];
		blockedRoad = new boolean[gridSize][gridSize];
	}

	/*
	 * Se actualiza la posición de Car. se setea la imagen. instance of para saber
	 * si el nodo es un Button y se castea para usar sus métodos.
	 */
	public synchronized void updateCarPosition(int prevRow, int prevCol, int row, int col, Car car) {
		if (grid == null)
			return;

		if (row < 0 || col < 0 || row >= gridCarPositions.length || col >= gridCarPositions[0].length) {
			System.out.println("Coordenada fuera de rango: (" + row + "," + col + ")");
			return;
		}

		if (blockedRoad[row][col]) {
			System.out.println("Celda bloqueada (" + row + "," + col + ") - Car " + car.getId() + " no puede avanzar.");
			return;
		}

		Car otro = gridCarPositions[row][col];
		if (otro != null && otro != car) {
			operateIncident(row, col, car, otro);
			return;
		}

		if (prevRow >= 0 && prevCol >= 0 && prevRow < gridCarPositions.length && prevCol < gridCarPositions[0].length) {
			gridCarPositions[prevRow][prevCol] = null;
		}

		gridCarPositions[row][col] = car;

		Platform.runLater(() -> {
			Node prevTarget = null;
			Node target = null;
			for (Node node : grid.getChildren()) {
				Integer r = GridPane.getRowIndex(node);
				Integer c = GridPane.getColumnIndex(node);
				if (r == null)
					r = 0;
				if (c == null)
					c = 0;

				if (r == prevRow && c == prevCol) {
					prevTarget = node;
				}
				if (r == row && c == col) {
					target = node;
				}
			}

			if (prevTarget instanceof Button) {
				((Button) prevTarget).setGraphic(null);
				((Button) prevTarget).setStyle("");
			}

			if (target instanceof Button) {
				Image carImage = new Image(getClass().getResourceAsStream("/img/carro.png"));
				ImageView carView = new ImageView(carImage);
				carView.setFitWidth(20);
				carView.setFitHeight(20);
				((Button) target).setGraphic(carView);
			}
		});
	}

	/*
	 * retorna el Button en la posición deseada.
	 */
	public Button getButtonAt(int i, int j) {
		if (grid == null)
			return null;
		for (Node node : grid.getChildren()) {
			Integer r = GridPane.getRowIndex(node);
			Integer c = GridPane.getColumnIndex(node);
			if (r == null)
				r = 0;
			if (c == null)
				c = 0;
			if (r == i && c == j && node instanceof Button) {
				return (Button) node;
			}
		}
		return null;
	}

	/*
	 * Lo que hace este método es operar el incidente, bloqueando la calle donde
	 * sucedió. Forma un objeto Incident que lleva los integrantes del evento y los
	 * almacena en una lista para poder colocar eso en la tabla del programa.
	 */
	public void operateIncident(int i, int j, Car c1, Car c2) {
		blockedRoad[i][j] = true;

		Incident choque = new Incident("CHOQUE", i, j,
				"Auto " + c1.getId() + " y Auto " + c2.getId() + " colisionaron.");
		controller.registerIncident(choque);
		System.out.println("CHOQUE DETECTADO: " + choque.toString());

		Platform.runLater(() -> {
			Button btn = getButtonAt(i, j);
			if (btn != null) {
				Image img = new Image(getClass().getResourceAsStream("/img/choque.png"));
				ImageView iv = new ImageView(img);
				iv.setFitWidth(20);
				iv.setFitHeight(20);
				btn.setGraphic(iv);
			}
		});

		gridCarPositions[i][j] = null;

		new Thread(() -> {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			Platform.runLater(() -> {
				Button btnClear = getButtonAt(i, j);
				if (btnClear != null) {
					btnClear.setGraphic(null);
					btnClear.setStyle("");
				}
				blockedRoad[i][j] = false;
				System.out.println("Calle liberada en (" + i + "," + j + ")");
			});
		}).start();
	}

	/*
	 * Método para el evento de reparación de calle. Toma toda la calle y le agrega
	 * imagenes. Bloquea la calla con el boolean.
	 */
	public void blockStreet(NodeRoad road) {
		Graph graph = GraphRoad.getGraph();
		if (graph == null)
			return;

		NodeVertex current = graph.getVertices().getFirst();

		while (current != null) {
			NodeV node = current.getNodeV();

			RoadList[] listas = { node.getxRoads(), node.getyRoads() };

			for (RoadList list : listas) {
				NodeRoad temp = list.getFirst();

				boolean belong = false;
				while (temp != null) {
					if (temp.getI() == road.getI() && temp.getJ() == road.getJ()) {
						belong = true;
						break;
					}
					temp = temp.getNext();
				}

				if (belong) {
					temp = list.getFirst();
					while (temp != null) {
						int i = temp.getI();
						int j = temp.getJ();
						blockedRoad[i][j] = true;
						final RoadList listCopia = list;
						new Thread(() -> {
							try {
								Thread.sleep(15000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}

							Platform.runLater(() -> {
								NodeRoad temp2 = listCopia.getFirst();
								while (temp2 != null) {
									int k = temp2.getI();
									int m = temp2.getJ();
									blockedRoad[k][m] = false;
									Button bClear = getButtonAt(k, m);
									if (bClear != null) {
										bClear.setGraphic(null);
										bClear.setStyle("");
									}
									temp2 = temp2.getNext();
								}
								System.out.println("Reparación finalizada y calle liberada.");
							});
						}).start();

						Incident r = new Incident("REPARACION", i, j, "Reparación en calle (" + i + "," + j + ")");
						controller.registerIncident(r);
						System.out.println("Reparación en (" + i + "," + j + ")");
						final Button btn = getButtonAt(i, j);
						Platform.runLater(() -> {
							if (btn != null) {
								Image img = new Image(getClass().getResourceAsStream("/img/reparacion.png"));
								ImageView iv = new ImageView(img);
								iv.setFitWidth(20);
								iv.setFitHeight(20);
								btn.setGraphic(iv);
							}
						});

						temp = temp.getNext();
					}
					return;
				}
			}

			current = current.getNext();
		}

	}

	/*
	 * Para saber si en i y j pos está bloqueado.
	 */
	public boolean isBlocked(int i, int j) {
		return blockedRoad[i][j];
	}

	/*
	 * genera el Objeto Car, el hilo. El Car se adentra al grafo. Puede andar en
	 * colas y NodeV. Se coloca de manera aleatoria en los vértices, no en las
	 * calles en si.
	 */
	public void generateCar() {
		Graph graph = GraphRoad.getGraph();
		if (graph == null || graph.getVertices() == null)
			return;

		VerticesList vList = graph.getVertices();
		if (LogicVerticesList.isEmpty(vList))
			return;

		int size = LogicVerticesList.size(vList);
		Random r = new Random();
		int start;
		int end;
		do {
			start = r.nextInt(size);
			end = r.nextInt(size);
		} while (start == end);

		NodeVertex startV = vList.getFirst();
		for (int i = 0; i < start && startV != null; i++) {
			startV = startV.getNext();
		}

		NodeVertex endVertex = vList.getFirst();
		for (int i = 0; i < end && endVertex != null; i++) {
			endVertex = endVertex.getNext();
		}

		if (startV == null || endVertex == null)
			return;

		Car car = new Car(startV.getNodeV(), endVertex.getNodeV(), this);

		LogicQueue.add(car, startV.getNodeV().getCars());

		Thread carThread = new Thread(car);
		carThread.setDaemon(true);
		carThread.start();
	}

	public Car[][] getGridCarPositions() {
		return gridCarPositions;
	}
}
