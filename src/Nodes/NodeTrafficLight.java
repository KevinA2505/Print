package Nodes;

public class NodeTrafficLight {
	private String direction; // "X" o "Y"
	private boolean isGreen;
	private NodeTrafficLight next;

	/*
	 * Crea un nuevo nodo de semáforo indicando su dirección y color inicial. Más
	 * que su color es su condición para conocer y verificar cuando un Car puede
	 * circular.
	 */
	public NodeTrafficLight(String direction, boolean isGreen) {
		this.direction = direction;
		this.isGreen = isGreen;
		this.next = null;
	}

	public String getDirection() {
		return direction;
	}

	public boolean isGreen() {
		return isGreen;
	}

	public void setGreen(boolean isGreen) {
		this.isGreen = isGreen;
	}

	public NodeTrafficLight getNext() {
		return next;
	}

	public void setNext(NodeTrafficLight next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return direction + ": " + (isGreen ? "VERDE" : "ROJO");
	}
}
