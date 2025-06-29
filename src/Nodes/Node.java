package Nodes;

public class Node {
    private NodeE nodeE;
    private Node next;
    
    public Node(NodeE nodeE) {
        this.nodeE = nodeE;
        this.next = null;
    }
    
    public NodeE getNodeE() {
        return nodeE;
    }
    
    public void setNodeE(NodeE nodeE) {
        this.nodeE = nodeE;
    }
    
    public Node getNext() {
        return next;
    }
    
    public void setNext(Node next) {
        this.next = next;
    }
}