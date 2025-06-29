package Structures;

import domain.Incident;

public class IncidentList {
    private Incident first;
    private Incident last;

    public IncidentList() {
        this.first = null;
        this.last = null;
    }

    public void add(Incident incident) {
        if (first == null) {
            first = incident;
            last = incident;
        } else {
            last.setNext(incident);
            last = incident;
        }
    }

    public Incident getFirst() { return first; }

    public boolean isEmpty() {
        return first == null;
    }

    public void printList() {
        Incident current = first;
        while (current != null) {
            System.out.println(current.toString());
            current = current.getNext();
        }
    }
}
