package domain;

import LogicStructures.LogicVerticesList;
import Nodes.NodeV;
import Structures.Graph;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class RoadsGrid {

	public static GridPane generateGrid(int n) {
		GraphRoad.resetGraph();
		GridPane g = new GridPane();
		int gridSize = n * n + n + 1;
		double p = 100.0 / (double) gridSize;

		for (int i = 0; i < gridSize; i++) {
			RowConstraints r = new RowConstraints();
			r.setPercentHeight(p);
			g.getRowConstraints().add(r);
			ColumnConstraints c = new ColumnConstraints();
			c.setPercentWidth(p);
			g.getColumnConstraints().add(c);
		}

		Graph roadGraph = GraphRoad.getGraph();

		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				boolean isRoad = isH(row, n) || isV(col, n);

				if (isRoad) {
					Button b = new Button(row + "," + col);
					b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
					if (n >= 6) {
						b.setPrefSize(2, 2);
					} else {
						b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
						b.setMinSize(5, 5); // opcional

					}
					g.add(b, col, row); // 1️⃣ Primero el botón

					// Si debe ser intersección (vértice del grafo)
					if (shouldCreateVertex(row, col, n, gridSize)) {
						int vertexId = generateVertexId(row, col);
						LogicVerticesList.add(vertexId, roadGraph.getVertices());

						NodeV node = roadGraph.getVertices().getLast().getNodeV();

						TrafficLightView view = new TrafficLightView();
						node.setTrafficLightView(view);

						g.add(view.getContainer(), col, row);
					}
				}
			}
		}

		Roads.assignRoadsToVertices(n, roadGraph);
		TrafficPatternGenerator.generateTrafficPattern(n, roadGraph);

		return g;
	}

	private static boolean shouldCreateVertex(int row, int col, int n, int gridSize) {
		boolean isHorizontalRoad = isH(row, n);
		boolean isVerticalRoad = isV(col, n);
		return isHorizontalRoad && isVerticalRoad;
	}

	private static int generateVertexId(int row, int col) {
		return row * 1000 + col;
	}

	private static boolean isH(int row, int n) {
		return row % (n + 1) == 0;
	}

	private static boolean isV(int col, int n) {
		return col % (n + 1) == 0;
	}
}
