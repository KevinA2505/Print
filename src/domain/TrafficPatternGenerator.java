package domain;

import java.util.Random;

import LogicStructures.LogicGraph;
import Structures.Graph;

public class TrafficPatternGenerator {
	
	public static void generateTrafficPattern(int n, Graph graph) {
		int step = n + 1;
		int gridSize = n * n + n + 1;
		generateHorizontalConnections(n, step, gridSize, graph);
		generateVerticalConnections(n, step, gridSize, graph);
	}

	private static void generateHorizontalConnections(int n, int step, int gridSize, Graph graph) {
		int[] vertexRows = getVertexPositions(step, gridSize);
		for (int i = 0; i < vertexRows.length; i++) {
			int row = vertexRows[i];
			int[] rowVertices = getVerticesInRow(row, step, gridSize);
			boolean leftToRight = (i % 2 == 0);
			if (leftToRight) {
				connectSequentially(rowVertices, graph);
			} else {
				connectReverseSequentially(rowVertices, graph);
			}
		}
	}

	private static void generateVerticalConnections(int n, int step, int gridSize, Graph graph) {
		int[] vertexCols = getVertexPositions(step, gridSize);
		for (int i = 0; i < vertexCols.length; i++) {
			int col = vertexCols[i];
			int[] colVertices = getVerticesInColumn(col, step, gridSize);
			boolean topToBottom = (i % 2 == 1);
			if (topToBottom) {
				connectSequentially(colVertices, graph);
			} else {
				connectReverseSequentially(colVertices, graph);
			}
		}
	}

	private static int[] getVertexPositions(int step, int gridSize) {
		int count = 0;
		for (int i = 0; i < gridSize; i += step) {
			count++;
		}
		int[] positions = new int[count];
		int index = 0;
		for (int i = 0; i < gridSize; i += step) {
			positions[index++] = i;
		}
		return positions;
	}

	private static int[] getVerticesInRow(int row, int step, int gridSize) {
		int count = 0;
		for (int col = 0; col < gridSize; col += step) {
			count++;
		}
		int[] vertices = new int[count];
		int index = 0;
		for (int col = 0; col < gridSize; col += step) {
			vertices[index++] = generateVertexId(row, col);
		}
		return vertices;
	}

	private static int[] getVerticesInColumn(int col, int step, int gridSize) {
		int count = 0;
		for (int row = 0; row < gridSize; row += step) {
			count++;
		}
		int[] vertices = new int[count];
		int index = 0;
		for (int row = 0; row < gridSize; row += step) {
			vertices[index++] = generateVertexId(row, col);
		}
		return vertices;
	}

	private static void connectSequentially(int[] vertices, Graph graph) {
		for (int i = 0; i < vertices.length - 1; i++) {
			int origin = vertices[i];
			int destination = vertices[i + 1];
			LogicGraph.addEdge(origin, destination, new Random().nextInt(8) + 3, graph);
		}
	}

	private static void connectReverseSequentially(int[] vertices, Graph graph) {
		for (int i = vertices.length - 1; i > 0; i--) {
			int origin = vertices[i];
			int destination = vertices[i - 1];
			LogicGraph.addEdge(origin, destination, new Random().nextInt(8) + 3, graph);
		}
	}

	private static int generateVertexId(int row, int col) {
		return row * 1000 + col;
	}
}