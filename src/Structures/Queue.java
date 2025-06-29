package Structures;

import Nodes.QueueNode;

public class Queue {

	private QueueNode first;

	public Queue() {
		this.first = null;
	}

	public QueueNode getFirst() {
		return first;
	}

	public void setFirst(QueueNode first) {
		this.first = first;
	}

}
