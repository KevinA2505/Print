package Structures;

import Nodes.Node;

public class EdgeList {

	private Node first;
	private Node last;

	public EdgeList() {
		this.first = null;
		this.last = null;
	}

	public Node getFirst() {
		return first;
	}

	public void setFirst(Node first) {
		this.first = first;
	}

	public Node getLast() {
		return last;
	}

	public void setLast(Node last) {
		this.last = last;
	}
}
