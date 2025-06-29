package Structures;

import Nodes.NodeVertex;

public class VerticesList {
    
    private NodeVertex first;
    private NodeVertex last;
    
    public VerticesList() {
        this.first = null;
        this.last = null;
    }
    
    public NodeVertex getFirst() {
        return first;
    }
    
    public void setFirst(NodeVertex first) {
        this.first = first;
    }
    
    public NodeVertex getLast() {
        return last;
    }
    
    public void setLast(NodeVertex last) {
        this.last = last;
    }
}