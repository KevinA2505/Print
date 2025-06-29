package Nodes;

public class NodeVertex {
    private NodeV nodeV;
    private NodeVertex next;
    
    public NodeVertex(NodeV nodeV) {
        this.nodeV = nodeV;
        this.next = null;
    }
    
    public NodeV getNodeV() {
        return nodeV;
    }
    
    public void setNodeV(NodeV nodeV) {
        this.nodeV = nodeV;
    }
    
    public NodeVertex getNext() {
        return next;
    }
    
    public void setNext(NodeVertex next) {
        this.next = next;
    }
}