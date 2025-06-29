package Structures;

public class Graph {

	private VerticesList vertices;

	public Graph() {
		this.vertices = new VerticesList();
	}

	public Graph(VerticesList vertices) {
		this.vertices = vertices;
	}

	public VerticesList getVertices() {
		return vertices;
	}

	public void setVertices(VerticesList vertices) {
		this.vertices = vertices;
	}
}
