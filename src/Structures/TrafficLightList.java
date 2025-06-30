package Structures;

import Nodes.NodeTrafficLight;

/*
 * Lista enlazada para almacenar los semáforos de una intersección.
 */
public class TrafficLightList {
	private NodeTrafficLight first;
	private NodeTrafficLight last;

        /*
         * Inicializa la lista vacía.
         */
        public TrafficLightList() {
                this.first = null;
                this.last = null;
        }

	public NodeTrafficLight getFirst() {
		return first;
	}

	public void setFirst(NodeTrafficLight first) {
		this.first = first;
	}

	public NodeTrafficLight getLast() {
		return last;
	}

	public void setLast(NodeTrafficLight last) {
		this.last = last;
	}

        /*
         * Indica si no existe ningún semáforo en la lista.
         */
        public boolean isEmpty() {
                return first == null;
        }
}
