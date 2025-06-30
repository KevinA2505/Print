package domain;

import LogicStructures.LogicTrafficLightList;
import Nodes.NodeTrafficLight;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.TrafficLightList;
import javafx.application.Platform;

import java.util.Random;

/*
 * Controla de forma cíclica el estado de los semáforos de cada intersección.
 */
public class TrafficLightController implements Runnable {
    private Graph graph;

    private static final String GREEN_STYLE = "-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 9px;";
    private static final String RED_STYLE = "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 9px;";

    /*
     * Recibe el grafo con las intersecciones que poseen semáforos.
     */
    public TrafficLightController(Graph graph) {
        this.graph = graph;
    }

    /*
     * Recorre cada intersección del grafo y lanza un hilo que gestiona su ciclo
     * de semáforos.
     */
    @Override
    public void run() {
        if (graph == null || graph.getVertices() == null) return;

        NodeVertex current = graph.getVertices().getFirst();

        while (current != null) {
            NodeV node = current.getNodeV();
            new Thread(() -> manageTrafficLightCycle(node)).start();
            current = current.getNext();
        }
    }

    /*
     * Ciclo que alterna el estado de los semáforos de un nodo con un retardo
     * aleatorio entre cambios.
     */
    private void manageTrafficLightCycle(NodeV node) {
        Random rand = new Random();
        TrafficLightList tList = node.getTrafficLights();
        TrafficLightView tView = node.getTrafficLightView();

        if (tList == null || tView == null) return;

        while (true) {
            int delay = 3000 + rand.nextInt(4000); // 3 a 7 segundos

            LogicTrafficLightList.toggleLights(tList);

            NodeTrafficLight xLight = LogicTrafficLightList.findByDirection(tList, "X");
            NodeTrafficLight yLight = LogicTrafficLightList.findByDirection(tList, "Y");

            Platform.runLater(() -> {
                if (xLight != null) {
                    tView.getxLight().setStyle(xLight.isGreen() ? GREEN_STYLE : RED_STYLE);
                }
                if (yLight != null) {
                    tView.getyLight().setStyle(yLight.isGreen() ? GREEN_STYLE : RED_STYLE);
                }
            });

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}