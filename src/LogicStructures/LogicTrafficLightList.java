package LogicStructures;

import Nodes.NodeTrafficLight;
import Structures.TrafficLightList;

/*
 * Métodos utilitarios para manejar listas de semáforos.
 */
public class LogicTrafficLightList {

        /*
         * Verifica si la lista no contiene ningún elemento.
         */
        public static boolean isEmpty(TrafficLightList list) {
                return list.getFirst() == null;
        }

        /*
         * Agrega un nuevo semáforo al final de la lista.
         */
        public static void add(TrafficLightList list, String direction, boolean isGreen) {
		if (isEmpty(list)) {
			NodeTrafficLight newNode = new NodeTrafficLight(direction, isGreen);
			list.setFirst(newNode);
			list.setLast(newNode);
			return;
		}
		list.getLast().setNext(new NodeTrafficLight(direction, isGreen));
		list.setLast(list.getLast().getNext());
	}

        /*
         * Cambia el estado de todos los semáforos de la lista.
         */
        public static void toggleLights(TrafficLightList list) {
                NodeTrafficLight current = list.getFirst();
                while (current != null) {
                        current.setGreen(!current.isGreen());
                        current = current.getNext();
                }
        }

        /*
         * Busca un semáforo por su dirección ("X" o "Y").
         */
        public static NodeTrafficLight findByDirection(TrafficLightList list, String direction) {
                NodeTrafficLight current = list.getFirst();
                while (current != null) {
                        if (current.getDirection().equalsIgnoreCase(direction)) {
                                return current;
                        }
                        current = current.getNext();
                }
                return null;
        }

        /*
         * Devuelve una cadena con el estado de todos los semáforos.
         */
        public static String printList(TrafficLightList list) {
		String result = "";
		NodeTrafficLight current = list.getFirst();
		while (current != null) {
			result += current.toString() + " | ";
			current = current.getNext();
		}
		return result;
	}
}
