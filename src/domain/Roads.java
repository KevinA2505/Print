package domain;

import LogicStructures.LogicRoadList;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;

/*
 * Utilidades para asociar las coordenadas de las "carreteras" que
 * parten de cada intersección del grafo.
 */
public final class Roads {

        /*
         * Clase de solo métodos estáticos, se evita la instanciación.
         */
        private Roads() {
        }

        /*
         * Recorre todos los vértices del grafo y llena sus listas de
         * carreteras horizontales y verticales según su posición en la
         * cuadrícula.
         */
        public static void assignRoadsToVertices(int n, Graph roadGraph) {
                if (roadGraph == null || roadGraph.getVertices() == null)
                        return;

                // Tamaño del bloque de calles y tamaño total de la cuadrícula
                final int block = n + 1;
                final int grid = n * n + n + 1;

                NodeVertex current = roadGraph.getVertices().getFirst();
                while (current != null) {
                        NodeV origin = current.getNodeV();
                        int id = origin.getData();
                        int row = id / 1000;
                        int col = id % 1000;

                        // Sentidos de exploración para las carreteras horizontales y verticales
                        int dirH = ((row / block) % 2 == 0) ? +1 : -1;
                        populateRoadList(row, col, 0, dirH, origin.getxRoads(), block, grid);
                        int dirV = ((col / block) % 2 == 0) ? -1 : +1;
                        populateRoadList(row, col, dirV, 0, origin.getyRoads(), block, grid);

                        current = current.getNext();
                }
        }

        /*
         * A partir de la intersección dada, avanza en la dirección
         * indicada (dr, dc) y agrega todas las coordenadas de la
         * carretera hasta llegar a otra intersección o salir del
         * tablero.
         */
        private static void populateRoadList(int row, int col, int dr, int dc, RoadList roadList, int block, int grid) {
                int r = row + dr;
                int c = col + dc;

                while (isInside(r, c, grid) && !isVertex(r, c, block)) {
                        LogicRoadList.add(r, c, roadList);
                        r += dr;
                        c += dc;
                }
        }

        /*
         * Verifica que la posición (r, c) esté dentro de los límites del
         * tablero.
         */
        private static boolean isInside(int r, int c, int size) {
                return r >= 0 && r < size && c >= 0 && c < size;
        }

        /*
         * Comprueba si la posición corresponde con una intersección del
         * grafo, es decir, si coincide con los múltiplos del tamaño de
         * bloque.
         */
        private static boolean isVertex(int r, int c, int block) {
                return (r % block == 0) && (c % block == 0);
        }
}
