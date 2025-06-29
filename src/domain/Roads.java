package domain;

import LogicStructures.LogicRoadList;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;

public final class Roads {

	private Roads() {}

	public static void assignRoadsToVertices(int n, Graph roadGraph) {
		if (roadGraph == null || roadGraph.getVertices() == null)
			return;

		final int block = n + 1;
		final int grid = n * n + n + 1;

		NodeVertex current = roadGraph.getVertices().getFirst();
		while (current != null) {
			NodeV origin = current.getNodeV();
			int id = origin.getData();
			int row = id / 1000;
			int col = id % 1000;

			int dirH = ((row / block) % 2 == 0) ? +1 : -1;
			populateRoadList(row, col, 0, dirH, origin.getxRoads(), block, grid);
			int dirV = ((col / block) % 2 == 0) ? -1 : +1;
			populateRoadList(row, col, dirV, 0, origin.getyRoads(), block, grid);

			current = current.getNext();
		}
	}

	private static void populateRoadList(int row, int col, int dr, int dc, RoadList roadList, int block, int grid) {
		int r = row + dr;
		int c = col + dc;

		while (isInside(r, c, grid) && !isVertex(r, c, block)) {
			LogicRoadList.add(r, c, roadList);
			r += dr;
			c += dc;
		}
	}

	private static boolean isInside(int r, int c, int size) {
		return r >= 0 && r < size && c >= 0 && c < size;
	}

	private static boolean isVertex(int r, int c, int block) {
		return (r % block == 0) && (c % block == 0);
	}
}
