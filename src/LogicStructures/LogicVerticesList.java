package LogicStructures;

import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.VerticesList;

public class LogicVerticesList {

	public static boolean isEmpty(VerticesList verticesList) {
		return verticesList.getFirst() == null;
	}

	public static void add(int data, VerticesList verticesList) {
		if (contains(data, verticesList)) {
			return;
		}

		if (isEmpty(verticesList)) {
			NodeVertex newNode = new NodeVertex(new NodeV(data));
			verticesList.setFirst(newNode);
			verticesList.setLast(newNode);
		} else {
			NodeVertex newNode = new NodeVertex(new NodeV(data));
			verticesList.getLast().setNext(newNode);
			verticesList.setLast(newNode);
		}
	}

	public static boolean contains(int data, VerticesList verticesList) {
		if (isEmpty(verticesList)) {
			return false;
		}

		NodeVertex current = verticesList.getFirst();
		while (current != null) {
			if (current.getNodeV().getData() == data) {
				return true;
			}
			current = current.getNext();
		}
		return false;
	}

	public static void removeFirst(VerticesList verticesList) {
		if (isEmpty(verticesList)) {
			return;
		}

		NodeVertex temp = verticesList.getFirst();
		if (temp.getNext() == null) {
			verticesList.setFirst(null);
			verticesList.setLast(null);
		} else {
			verticesList.setFirst(temp.getNext());
		}
		temp.setNext(null);
	}

	public static int recurSize(NodeVertex head) {
		if (head == null) {
			return 0;
		}
		return 1 + recurSize(head.getNext());
	}

	public static void removeLast(VerticesList verticesList) {
		if (isEmpty(verticesList)) {
			return;
		}

		NodeVertex temp = verticesList.getFirst();

		// Si solo hay un elemento
		if (temp.getNext() == null) {
			verticesList.setFirst(null);
			verticesList.setLast(null);
			return;
		}

		// Buscar el penúltimo nodo
		while (temp.getNext() != null && temp.getNext() != verticesList.getLast()) {
			temp = temp.getNext();
		}

		if (temp.getNext() == verticesList.getLast()) {
			verticesList.setLast(temp);
			temp.setNext(null);
		}
	}

	public static int size(VerticesList verticesList) {
		NodeVertex temp = verticesList.getFirst();
		int size = 0;
		while (temp != null) {
			size++;
			temp = temp.getNext();
		}
		return size;
	}

	public static String printList(VerticesList verticesList) {
		if (isEmpty(verticesList)) {
			return "";
		}

		NodeVertex temp = verticesList.getFirst();
		StringBuilder text = new StringBuilder();

		while (temp != null) {
			text.append(temp.getNodeV().toString());
			if (temp.getNext() != null) {
				text.append(", ");
			}
			temp = temp.getNext();
		}

		return text.toString();
	}

}