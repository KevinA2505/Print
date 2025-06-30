package business;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import LogicStructures.LogicRoadList;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;
import domain.CongestedRoad;
import domain.GraphRoad;
import domain.Incident;
import domain.Car;
import javafx.scene.control.Button;

public class CongestionManager {
    private CarManager carManager;
    private MainController controller;
    private ObservableList<CongestedRoad> congestedObservable;

    public CongestionManager(CarManager carManager, MainController controller, ObservableList<CongestedRoad> congestedObservable) {
        this.carManager = carManager;
        this.controller = controller;
        this.congestedObservable = congestedObservable;
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
                    controller.registerIncident(inc);
                    System.out.println("Congestión detectada: " + inc);
                } else if (autosEnCalle < 3) {
                    clearOrangeRoad(lista);
                }
            }
            current = current.getNext();
        }

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
        Car[][] cars = carManager.getGridCarPositions();
        while (current != null) {
            int i = current.getI();
            int j = current.getJ();
            if (cars[i][j] != null) {
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
                Button btn = carManager.getButtonAt(current.getI(), current.getJ());
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
                Button btn = carManager.getButtonAt(current.getI(), current.getJ());
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
                    Thread.sleep(2000);
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
