package domain;

import LogicStructures.LogicRoadList;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;

public final class RoadLister {

    private RoadLister() { 
    	
    }
    
    public static void print(Graph graph) {
        if (graph == null || graph.getVertices() == null) {
            System.out.println("Grafo vacío o nulo.");
            return;
        }

        NodeVertex current = graph.getVertices().getFirst();
        while (current != null) {
            NodeV node = current.getNodeV();
            System.out.println("Nodo " + toCoord(node.getData()));

            System.out.print("  X → ");
            printRoadList(node.getxRoads());

            System.out.print("  Y → ");
            printRoadList(node.getyRoads());
            System.out.println();

            current = current.getNext();
        }
    }
    
    private static void printRoadList(RoadList list) {
        if (list == null || LogicRoadList.isEmpty(list)) {
            System.out.println("(vacío)");
            return;
        }
        NodeRoad curr = list.getFirst();
        while (curr != null) {
            System.out.print("(" + curr.getI() + "," + curr.getJ() + ") ");
            curr = curr.getNext();
        }
        System.out.println();
    }

    private static String toCoord(int id) {
        int row = id / 1000;
        int col = id % 1000;
        return row + "," + col;
    }
}
