package business;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.application.Platform;
import LogicStructures.LogicQueue;
import LogicStructures.LogicRoadList;
import LogicStructures.LogicVerticesList;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import Structures.Graph;
import Structures.IncidentList;
import Structures.RoadList;
import Structures.VerticesList;
import domain.Car;
import domain.GraphRoad;
import domain.Incident;
import domain.CongestedRoad;
import domain.RoadLister;
import domain.RoadsGrid;
import domain.TrafficLightController;
import javafx.event.ActionEvent;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainController {
	@FXML
	private Pane pGrid;
	@FXML
	private Pane pData;
	@FXML
	private Spinner<Integer> sSize;
	@FXML
	private TableView<Incident> tVIncidents;
	@FXML
	private TableColumn<Incident, String> tCIncidentName;
	@FXML
	private TableColumn<Incident, Integer> tCIncidentI;
	@FXML
	private TableColumn<Incident, Integer> tCIncidentJ;
	@FXML
    private TableView<CongestedRoad> tVCongestedRoads;
    @FXML
    private TableColumn<CongestedRoad, String> tCCongestedRoadCoord;
	@FXML
	private Button bEvent;
	@FXML
	private Button bGenerateCar;
	@FXML
	private Button bShowGraph;
	@FXML
	private Button bRoads;

	private GridPane grid;

	private Car[][] gridCarPositions = new Car[20][20]; // ajusta al tamaño de el grid real
    private boolean[][] isBLockedRoad = new boolean[20][20];
    private IncidentList incidentList = new IncidentList();
    private ObservableList<Incident> incidentsObservable = FXCollections.observableArrayList();
    private ObservableList<CongestedRoad> congestedObservable = FXCollections.observableArrayList();

    private void registerIncident(Incident inc) {
            incidentList.add(inc);
            Platform.runLater(() -> incidentsObservable.add(inc));
    }

	@FXML
	private void initialize() {
		/*
		 * Estableceré un limite de 5 por un tema de espacio en la ventana
		 */
		sSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 5, 3));
		sSize.valueProperty().addListener((o, v, n) -> draw());
		draw();

		// Controlador de semáforos al arrancar
		Thread tLightThread = new Thread(new TrafficLightController(GraphRoad.getGraph()));
		tLightThread.setDaemon(true);
		tLightThread.start();
		initEvents();
		initCongestion();
		initTableEvents();
		initTableRoads();
	}

    @SuppressWarnings("unchecked")
	private void initTableEvents() {
            tCIncidentName = new TableColumn<>("Type");
            tCIncidentName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));

            tCIncidentI = new TableColumn<>("Row");
            tCIncidentI.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRow()).asObject());

            tCIncidentJ = new TableColumn<>("Col");
            tCIncidentJ.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCol()).asObject());

            tVIncidents.getColumns().setAll(tCIncidentName, tCIncidentI, tCIncidentJ);
            tVIncidents.setItems(incidentsObservable);

            Incident curr = incidentList.getFirst();
            while (curr != null) {
                    incidentsObservable.add(curr);
                    curr = curr.getNext();
            }
    }

    @SuppressWarnings("unchecked")
    private void initTableRoads() {
            tCCongestedRoadCoord = new TableColumn<>("Road");
            tCCongestedRoadCoord.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCoord()));

            TableColumn<CongestedRoad, Integer> carsCol = new TableColumn<>("Cars");
            carsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCars()).asObject());

            tVCongestedRoads.getColumns().setAll(tCCongestedRoadCoord, carsCol);
            tVCongestedRoads.setItems(congestedObservable);
    }

	private void draw() {
		/*
		 * Con 7, por ejemplo, se desborda el grid en la ventana, pero el código
		 * funciona igualmente.
		 */
		int a = sSize.getValue(); // tamaño 3 a 5
		GridPane g = RoadsGrid.generateGrid(a);
		grid = g;

		// Crear grid dinámico según tamaño real de nodos
		int gridSize = a * a + a + 1;
		gridCarPositions = new Car[gridSize][gridSize];
		isBLockedRoad = new boolean[gridSize][gridSize];

		g.prefWidthProperty().bind(pGrid.widthProperty());
		g.prefHeightProperty().bind(pGrid.heightProperty());
		g.maxWidthProperty().bind(pGrid.widthProperty());
		g.maxHeightProperty().bind(pGrid.heightProperty());
		g.minWidthProperty().bind(pGrid.widthProperty());
		g.minHeightProperty().bind(pGrid.heightProperty());

		pGrid.getChildren().setAll(g);
	}

	public synchronized void updateCarPosition(int prevRow, int prevCol, int row, int col, Car car) {
		if (grid == null)
			return;

		// Prevención de errores por coordenadas fuera de rango
		if (row < 0 || col < 0 || row >= gridCarPositions.length || col >= gridCarPositions[0].length) {
			System.out.println("Coordenada fuera de rango: (" + row + "," + col + ")");
			return;
		}

		if (isBLockedRoad[row][col]) {
			System.out.println("Celda bloqueada (" + row + "," + col + ") - Car " + car.getId() + " no puede avanzar.");
			return;
		}

		Car otro = gridCarPositions[row][col];
		if (otro != null && otro != car) {
			operateIncident(row, col, car, otro);
			return;
		}

		// Eliminar de posición anterior
		if (prevRow >= 0 && prevCol >= 0 && prevRow < gridCarPositions.length && prevCol < gridCarPositions[0].length) {
			gridCarPositions[prevRow][prevCol] = null;
		}

		// Asignar nueva posición
		gridCarPositions[row][col] = car;

		/*
		 * Aqui actualizamos interfaz por medio de runLater. La imagen del Car. Esto lo
		 * que hace es posicionar en un lado la imagen, y kas coordenadas quedan a un
		 * lado tambien.
		 */
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

	public void operateIncident(int i, int j, Car c1, Car c2) {
		isBLockedRoad[i][j] = true;

            Incident choque = new Incident("CHOQUE", i, j,
                            "Auto " + c1.getId() + " y Auto " + c2.getId() + " colisionaron.");
            registerIncident(choque);
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
				isBLockedRoad[i][j] = false;
				System.out.println("Calle liberada en (" + i + "," + j + ")");
			});
		}).start();
	}

	// Event Listener on Button[#bEvent].onAction
	@FXML
	public void toChooseEvent(ActionEvent event) {
		Graph graph = GraphRoad.getGraph();
		if (graph == null)
			return;

		NodeVertex current = graph.getVertices().getFirst();

		// Escoger nodo al azar
		int count = LogicVerticesList.size(graph.getVertices());
		int randIndex = new Random().nextInt(count);

		for (int i = 0; i < randIndex && current != null; i++) {
			current = current.getNext();
		}

		if (current == null)
			return;

		NodeV nodo = current.getNodeV();
		RoadList[] listas = { nodo.getxRoads(), nodo.getyRoads() };

		for (RoadList list : listas) {
			if (list != null && !LogicRoadList.isEmpty(list)) {
				int len = LogicRoadList.size(list);
				int randRoadIndex = new Random().nextInt(len);
				NodeRoad target = LogicRoadList.getAt(list, randRoadIndex);
				blockStreet(target);
				return;
			}
		}
	}

	// Event Listener on Button[#bGenerateCar].onAction
	@FXML
	public void toAddCar(ActionEvent event) {
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

	private Button getButtonAt(int i, int j) {
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
					// Bloquea toda la calle
					temp = list.getFirst();
					while (temp != null) {
						int i = temp.getI();
						int j = temp.getJ();
						isBLockedRoad[i][j] = true;

						// Al final de bloquearCallePorReparacion
						final RoadList listCopia = list; // capturamos la lista final para el thread

						new Thread(() -> {
							try {
								Thread.sleep(15000); // duración de la reparación 15 segs
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}

							Platform.runLater(() -> {
								NodeRoad temp2 = listCopia.getFirst();
								while (temp2 != null) {
									int k = temp2.getI();
									int m = temp2.getJ();

									isBLockedRoad[k][m] = false;

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

						// Agregar incidente
                                                Incident r = new Incident("REPARACION", i, j, "Reparación en calle (" + i + "," + j + ")");
                                                registerIncident(r);
                                                System.out.println("Reparación en (" + i + "," + j + ")");

						// Mostrar imagen visual
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
					return; // ya se encontró y bloqueó la calle, no seguir
				}
			}

			current = current.getNext();
		}

	}

	public void initEvents() {
		Thread event = new Thread(() -> {
			Random r = new Random();
			Graph graph = GraphRoad.getGraph();
			if (graph == null)
				return;

			while (true) {
				try {
					int waitTime = r.nextInt(11) + 10; // entre 10 y 20 segundos
					Thread.sleep(waitTime * 1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}

				NodeVertex current = graph.getVertices().getFirst();

				// Escoger nodo al azar
				int count = LogicVerticesList.size(graph.getVertices());
				int randIndex = r.nextInt(count);

				for (int i = 0; i < randIndex && current != null; i++) {
					current = current.getNext();
				}

				if (current == null)
					continue;

				NodeV nodo = current.getNodeV();
				RoadList[] listas = { nodo.getxRoads(), nodo.getyRoads() };

				for (RoadList list : listas) {
					if (list != null && !LogicRoadList.isEmpty(list)) {
						int len = LogicRoadList.size(list);
						int randRoadIndex = r.nextInt(len);
						NodeRoad target = LogicRoadList.getAt(list, randRoadIndex);

						// Reparación automática
						blockStreet(target);
						break;
					}
				}
			}
		});

		event.setDaemon(true);
		event.start();
	}

	public boolean isBlocked(int i, int j) { // metodo auxiliar
		return isBLockedRoad[i][j];
	}

	// Event Listener on Button[#bShowGraph].onAction
	@FXML
	public void toShowGraphInfoInConsole(ActionEvent event) {
		GraphRoad.displayGraph();

	}

	// Event Listener on Button[#bRoads].onAction
	@FXML
	public void toShowRoads(ActionEvent event) {
		Graph graph = GraphRoad.getGraph();
		RoadLister.print(graph);
	}

        public void detectCongestedRoad() {
                Graph graph = GraphRoad.getGraph();
                if (graph == null)
                        return;

                ObservableList<CongestedRoad> temp = FXCollections.observableArrayList();

                NodeVertex current = graph.getVertices().getFirst();
                while (current != null) {
                        NodeV nodo = current.getNodeV();

			RoadList[] listas = { nodo.getxRoads(), nodo.getyRoads() };

			for (RoadList lista : listas) {
                                if (lista == null || LogicRoadList.isEmpty(lista))
                                        continue;

                                int autosEnCalle = countingCarsInRoad(lista);
                                if (autosEnCalle > 0) {
                                        NodeRoad head = lista.getFirst();
                                        if (head != null) {
                                                temp.add(new CongestedRoad(head.getI(), head.getJ(), autosEnCalle));
                                        }
                                }
                                if (autosEnCalle >= 3 && autosEnCalle <= 5) {
                                        orangeRoad(lista);

					NodeRoad inicio = lista.getFirst();
                                        Incident inc = new Incident("CONGESTION", inicio.getI(), inicio.getJ(),
                                                        "Congestión detectada con " + autosEnCalle + " autos.");
                                        registerIncident(inc);
                                        System.out.println("Congestión detectada: " + inc);
				} else if (autosEnCalle < 3) {
					clearOrangeRoad(lista);
				}
			}
                        current = current.getNext();
                }

                // ordenar por cantidad de autos de forma descendente
                for (int i = 0; i < temp.size(); i++) {
                        for (int j = i + 1; j < temp.size(); j++) {
                                CongestedRoad a = temp.get(i);
                                CongestedRoad b = temp.get(j);
                                if (a.getCars() < b.getCars()) {
                                        temp.set(i, b);
                                        temp.set(j, a);
                                }
                        }
                }

                Platform.runLater(() -> {
                        congestedObservable.setAll(temp);
                });
        }

	private int countingCarsInRoad(RoadList lista) {
		int count = 0;
		NodeRoad current = lista.getFirst();
		while (current != null) {
			int i = current.getI();
			int j = current.getJ();
			if (gridCarPositions[i][j] != null) {
				count++;
			}
			current = current.getNext();
		}
		return count;
	}

	private void orangeRoad(RoadList lista) {
		Platform.runLater(() -> {
			NodeRoad current = lista.getFirst();
			while (current != null) {
				Button btn = getButtonAt(current.getI(), current.getJ());
				if (btn != null) {
					btn.setStyle("-fx-background-color: orange;");
				}
				current = current.getNext();
			}
		});
	}

	private void clearOrangeRoad(RoadList lista) {
		Platform.runLater(() -> {
			NodeRoad current = lista.getFirst();
			while (current != null) {
				Button btn = getButtonAt(current.getI(), current.getJ());
				if (btn != null) {
					btn.setStyle("");
				}
				current = current.getNext();
			}
		});
	}

	public void initCongestion() {
		Thread congestionThread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(2000); // cada 2 segundos
				} catch (InterruptedException e) {
					return;
				}
				detectCongestedRoad();
			}
		});
		congestionThread.setDaemon(true);
		congestionThread.start();
	}

}