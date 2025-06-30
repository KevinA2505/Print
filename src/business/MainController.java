package business;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import Structures.Graph;
import Structures.IncidentList;
import Nodes.NodeIncident;
import LogicStructures.LogicIncidentList;
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

	private IncidentList incidentList = new IncidentList();
	private ObservableList<Incident> incidentsObservable = FXCollections.observableArrayList();
	private ObservableList<CongestedRoad> congestedObservable = FXCollections.observableArrayList();

	private CarManager carManager;
	private EventManager eventManager;
	private CongestionManager congestionManager;

	private Thread trafficLightThread;

	/*
	 * Incia el spinner, hasta 5 porque en 6 y 7 se desborda de la ventaa. Igual el
	 * código funciona para esos valores, auqneu se limita por temas visuales.
	 * Tambien se dibuja el grafo. Tambien se llaman a inicar las tablas. Aqui se
	 * generan los Manager. Aquí se inica el controlador de semaforos.
	 */
	@FXML
	private void initialize() {
		/*
		 * Estableceré un limite de 5 por un tema de espacio en la ventana
		 */
		sSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 5, 3));
		sSize.valueProperty().addListener((o, v, n) -> draw());

		carManager = new CarManager(this);
		eventManager = new EventManager(carManager);
		congestionManager = new CongestionManager(carManager, this, congestedObservable);

		draw();

		// Controlador de semáforos al arrancar
		trafficLightThread = new Thread(new TrafficLightController(GraphRoad.getGraph()));
		trafficLightThread.setDaemon(true);
		trafficLightThread.start();

		eventManager.initEvents();
		congestionManager.initCongestion();
		initTableEvents();
		initTableRoads();
	}

	/*
	 * Tabla de eventos, donde se carga el tipo de evento y las cooredenadas.
	 */
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

		NodeIncident curr = incidentList.getFirst();
		while (curr != null) {
			incidentsObservable.add(curr.getIncident());
			curr = curr.getNext();
		}
	}

	/*
	 * Aqui se inicia la tabla de las calles con mayor número de carros. Se guarda
	 * las coordenadas más la cantidad ce Car en ella.
	 */
	@SuppressWarnings("unchecked")
	private void initTableRoads() {
		tCCongestedRoadCoord = new TableColumn<>("Road");
		tCCongestedRoadCoord.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCoord()));

		TableColumn<CongestedRoad, Integer> carsCol = new TableColumn<>("Cars");
		carsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCars()).asObject());

		tVCongestedRoads.getColumns().setAll(tCCongestedRoadCoord, carsCol);
		tVCongestedRoads.setItems(congestedObservable);
	}

	/*
	 * Este método se interpuso a la lógica anterior donde solo se aplicaba
	 * LogicIncidentList puesto a que ahora se neceista el obsrvable para actualizar
	 * la tabla.
	 */
	public void registerIncident(Incident inc) {
		LogicIncidentList.add(inc, incidentList);
		Platform.runLater(() -> incidentsObservable.add(inc));
	}

	/*
	 * Se dibuja el grafo en el GridPane usando el método generateGrid de RoadsGrid.
	 */
	private void draw() {
		/*
		 * Con 7, por ejemplo, se desborda el grid en la ventana, pero el código
		 * funciona igualmente.
		 */
		int a = sSize.getValue(); // tamaño 3 a 5
		GridPane g = RoadsGrid.generateGrid(a);

		// Crear grid dinámico según tamaño real de nodos
		int gridSize = a * a + a + 1;
		if (carManager != null) {
			carManager.initGrid(g, gridSize);
		}

		g.prefWidthProperty().bind(pGrid.widthProperty());
		g.prefHeightProperty().bind(pGrid.heightProperty());
		g.maxWidthProperty().bind(pGrid.widthProperty());
		g.maxHeightProperty().bind(pGrid.heightProperty());
		g.minWidthProperty().bind(pGrid.widthProperty());
		g.minHeightProperty().bind(pGrid.heightProperty());

		pGrid.getChildren().setAll(g);

		// Reiniciar control de semáforos para la nueva cuadrícula
		Thread newTrafficLightThread = new Thread(new TrafficLightController(GraphRoad.getGraph()));
		newTrafficLightThread.setDaemon(true);
		newTrafficLightThread.start();
		trafficLightThread = newTrafficLightThread;
	}

	// Event Listener on Button[#bEvent].onAction
	@FXML
	public void toChooseEvent(ActionEvent event) {
		if (eventManager != null) {
			eventManager.chooseEvent();
		}
	}

	// Event Listener on Button[#bGenerateCar].onAction
	@FXML
	public void toAddCar(ActionEvent event) {
		if (carManager != null) {
			carManager.generateCar();
		}
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

}