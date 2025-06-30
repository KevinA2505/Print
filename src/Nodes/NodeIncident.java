package Nodes;

import domain.Incident;

public class NodeIncident {
    private Incident incident;
    private NodeIncident next;

    public NodeIncident(Incident incident) {
        this.incident = incident;
        this.next = null;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public NodeIncident getNext() {
        return next;
    }

    public void setNext(NodeIncident next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return incident.toString();
    }
}
