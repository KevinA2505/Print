package Structures;

import Nodes.NodeTrafficLight;

public class TrafficLightList {
	private NodeTrafficLight first;
	private NodeTrafficLight last;

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

}
