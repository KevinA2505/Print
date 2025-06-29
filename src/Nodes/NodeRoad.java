package Nodes;


public class NodeRoad {

	private int i;
	private int j;
	private NodeRoad next;

	public NodeRoad(int i, int j) {
		super();
		this.i = i;
		this.j = j;
		this.next = null;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public NodeRoad getNext() {
		return next;
	}

	public void setNext(NodeRoad next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "(" + i + " ," + j + ")";
	}
}
