package Structures;

import Nodes.NodeIncident;

public class IncidentList {
    private NodeIncident first;
    private NodeIncident last;

    public IncidentList() {
        this.first = null;
        this.last = null;
    }

    public NodeIncident getFirst() {
        return first;
    }

    public void setFirst(NodeIncident first) {
        this.first = first;
    }

    public NodeIncident getLast() {
        return last;
    }

    public void setLast(NodeIncident last) {
        this.last = last;
    }
}
