package domain;

import Nodes.NodeVertex;
import LogicStructures.LogicTrafficLightList;
import Nodes.NodeTrafficLight;
import Structures.Graph;
import Structures.TrafficLightList;
import javafx.application.Platform;

public class TrafficLightController implements Runnable {
    private Graph graph;

    public TrafficLightController(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void run() {
        while (true) {
            try {
                NodeVertex current = graph.getVertices().getFirst();
                while (current != null) {
                    TrafficLightList tList = current.getNodeV().getTrafficLights();
                    TrafficLightView tView = current.getNodeV().getTrafficLightView();

                    if (tList != null && tView != null) {
                    	LogicTrafficLightList.toggleAll(tList);
                        //tList.toggleAll();

                        NodeTrafficLight xLight = tList.getFirst();
                        NodeTrafficLight yLight = xLight.getNext();

                        Platform.runLater(() -> {
                            if (xLight.isGreen()) {
                                tView.getxLight().setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 9px;");
                            } else {
                                tView.getxLight().setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 9px;");
                            }
                            if (yLight.isGreen()) {
                                tView.getyLight().setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 9px;");
                            } else {
                                tView.getyLight().setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 9px;");
                            }
                        });
                    }
                    current = current.getNext();
                }

                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
