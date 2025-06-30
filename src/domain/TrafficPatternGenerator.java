package domain;

import java.util.Random;

import LogicStructures.LogicGraph;
import Structures.Graph;

/*
 * Utilidad para generar conexiones de tráfico simulando un patrón de sentido
 * único alternado entre calles horizontales y verticales.
 */
public class TrafficPatternGenerator {
	
        /*
         * Genera un conjunto de conexiones unidireccionales para un grid de
         * tamaño n. Se alternan los sentidos horizontal y vertical para formar
         * un circuito.
         */
        public static void generateTrafficPattern(int n, Graph graph) {
		int step = n + 1;
		int gridSize = n * n + n + 1;
		generateHorizontalConnections(n, step, gridSize, graph);
		generateVerticalConnections(n, step, gridSize, graph);
	}

        /*
         * Conecta los vértices de cada fila alternando el sentido de la vía.
         */
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

        /*
         * Conecta los vértices de cada columna alternando también el sentido
         * para crear una ruta continua.
         */
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

        /*
         * Devuelve los índices de fila o columna donde se ubican los vértices
         * del grafo.
         */
        private static int[] getVertexPositions(int step, int gridSize) {
            int count = (gridSize + step - 1) / step;  // Math.ceil(gridSize / step)
            int[] positions = new int[count];
            for (int i = 0; i < count; i++) {
                positions[i] = i * step;
            }
            return positions;
        }

        /*
         * Lista los identificadores de vértices ubicados en una fila concreta.
         */
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

        /*
         * Similar a getVerticesInRow pero para las columnas del grid.
         */
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

        /*
         * Conecta los vértices del arreglo en orden, creando un camino directo.
         */
        private static void connectSequentially(int[] vertices, Graph graph) {
		for (int i = 0; i < vertices.length - 1; i++) {
			int origin = vertices[i];
			int destination = vertices[i + 1];
			LogicGraph.addEdge(origin, destination, new Random().nextInt(8) + 3, graph);
		}
	}

        /*
         * Conecta los vértices en sentido contrario al arreglo recibido.
         */
        private static void connectReverseSequentially(int[] vertices, Graph graph) {
		for (int i = vertices.length - 1; i > 0; i--) {
			int origin = vertices[i];
			int destination = vertices[i - 1];
			LogicGraph.addEdge(origin, destination, new Random().nextInt(8) + 3, graph);
		}
	}

        /*
         * Convierte la posición (fila, columna) en un identificador único para
         * el vértice.
         */
        private static int generateVertexId(int row, int col) {
                return row * 1000 + col;
        }
}