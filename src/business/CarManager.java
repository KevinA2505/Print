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
import data.CarInfo;
import data.JsonUtils;

public class CarManager {
	private GridPane grid;
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

	public synchronized void updateCarPosition(int prevRow, int prevCol, int row, int col, Car car) {
        if (grid == null) return;

        // si el carro está inactivo, no actualizar la grilla
        if (!car.isActive()) return;

        if (blockedRoad[row][col]) {
            System.out.println("Celda bloqueada (" + row + "," + col + ") - Car " + car.getId() + " no puede avanzar.");

            // esto se hace porque como que aparece un carro fantasma después del choque, entonces
            // esto borra el gráfico del último paso del carrito, basicamente, si se meueve, borre ese movimiento porque ya 
            //chocó
            
            Platform.runLater(() -> {
                Node prevTarget = null;
                for (Node node : grid.getChildren()) {
                    Integer r = GridPane.getRowIndex(node);
                    Integer c = GridPane.getColumnIndex(node);
                    if (r == null) r = 0;
                    if (c == null) c = 0;
                    if (r == prevRow && c == prevCol) prevTarget = node;
                }
                if (prevTarget instanceof Button) {
                    ((Button) prevTarget).setGraphic(null);
                    ((Button) prevTarget).setStyle("");
                }
            });

            gridCarPositions[prevRow][prevCol] = null; // Limpia referencia de la celda previa
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
		
		c1.deactivate();
	    c2.deactivate();

		gridCarPositions[i][j] = null; // Borramos el carro de la grilla

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

	public boolean isBlocked(int i, int j) {
		return blockedRoad[i][j];
	}

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

                try {
                        CarInfo info = new CarInfo(car.getId(), startV.getNodeV().getData(),
                                        endVertex.getNodeV().getData());
                        JsonUtils<CarInfo> ju = new JsonUtils<>("src/data/cars.json");
                        ju.save(info);
                } catch (Exception e) {
                        e.printStackTrace();
                }

                LogicQueue.add(car, startV.getNodeV().getCars());

                Thread carThread = new Thread(car);
                carThread.setDaemon(true);
                carThread.start();
	}

	public Car[][] getGridCarPositions() {
		return gridCarPositions;
	}
}