package LogicStructures;

import Nodes.NodeE;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.VerticesList;

public class LogicGraph {

	public static void addEdge(int origin, int destination, int weight, Graph graph) {
		VerticesList vList = graph.getVertices();

		if (!LogicVerticesList.contains(origin, vList) || !LogicVerticesList.contains(destination, vList)) {
			System.err.println("NO contains the origin or destination.");
			return;
		}

		NodeV a = getNode(origin, graph);
		NodeV b = getNode(destination, graph);

		LogicEdgeList.add(new NodeE(b, weight), a.getEdges());
	}

	private static NodeV getNode(int data, Graph graph) {
		if (LogicVerticesList.isEmpty(graph.getVertices()))
			return null;
		NodeVertex temp = graph.getVertices().getFirst();

		while (temp != null) {
			if (temp.getNodeV().getData() == data)
				return temp.getNodeV();
			temp = temp.getNext();
		}
		return null;

	}
}
