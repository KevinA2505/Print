package Nodes;

public class NodeE {
	private NodeV destination;
	private int weight;

	public NodeE(NodeV destination, int weight) {
		this.destination = destination;
		this.weight = weight;
	}

	public NodeV getDestination() {
		return destination;
	}

	public void setDestination(NodeV destination) {
		this.destination = destination;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return destination + " ," + weight;
	}

}
