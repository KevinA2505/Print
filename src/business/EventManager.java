package business;

import java.util.Random;

import LogicStructures.LogicRoadList;
import LogicStructures.LogicVerticesList;
import Structures.Graph;
import Structures.RoadList;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import domain.GraphRoad;

public class EventManager {
    private CarManager carManager;

    public EventManager(CarManager carManager) {
        this.carManager = carManager;
    }

    public void chooseEvent() {
        Graph graph = GraphRoad.getGraph();
        if (graph == null)
            return;

        NodeVertex current = graph.getVertices().getFirst();

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
                carManager.blockStreet(target);
                return;
            }
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

                chooseEvent();
            }
        });

        event.setDaemon(true);
        event.start();
    }
}

