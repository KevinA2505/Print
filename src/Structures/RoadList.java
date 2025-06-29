package Structures;

import Nodes.NodeRoad;

public class RoadList {

	private int nameCoord;
	private NodeRoad first;
	private NodeRoad last;
	
	public RoadList(int nameCoord) {
		this.nameCoord = nameCoord;
		this.first = null;
		this.last = null;
	}

	public int getNameCoord() {
		return nameCoord;
	}

	public void setNameCoord(int nameCoord) {
		this.nameCoord = nameCoord;
	}

	public NodeRoad getFirst() {
		return first;
	}

	public void setFirst(NodeRoad first) {
		this.first = first;
	}

	public NodeRoad getLast() {
		return last;
	}

	public void setLast(NodeRoad last) {
		this.last = last;
	}
}
