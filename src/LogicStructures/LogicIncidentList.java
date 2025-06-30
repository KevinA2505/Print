package LogicStructures;

import Nodes.NodeIncident;
import Structures.IncidentList;
import domain.Incident;

public class LogicIncidentList {

    public static boolean isEmpty(IncidentList list) {
        return list.getFirst() == null;
    }

    public static void add(Incident incident, IncidentList list) {
        NodeIncident newNode = new NodeIncident(incident);
        if (isEmpty(list)) {
            list.setFirst(newNode);
            list.setLast(newNode);
        } else {
            list.getLast().setNext(newNode);
            list.setLast(newNode);
        }
    }

    public static void removeFirst(IncidentList list) {
        if (isEmpty(list)) {
            return;
        }
        NodeIncident temp = list.getFirst();
        if (temp.getNext() == null) {
            list.setFirst(null);
            list.setLast(null);
        } else {
            list.setFirst(temp.getNext());
        }
        temp.setNext(null);
    }

    public static void removeLast(IncidentList list) {
        if (isEmpty(list)) {
            return;
        }
        NodeIncident temp = list.getFirst();
        if (temp.getNext() == null) {
            list.setFirst(null);
            list.setLast(null);
            return;
        }
        while (temp.getNext() != null && temp.getNext() != list.getLast()) {
            temp = temp.getNext();
        }
        if (temp.getNext() == list.getLast()) {
            list.setLast(temp);
            temp.setNext(null);
        }
    }

    public static int size(IncidentList list) {
        NodeIncident temp = list.getFirst();
        int size = 0;
        while (temp != null) {
            size++;
            temp = temp.getNext();
        }
        return size;
    }

    public static NodeIncident getAt(IncidentList list, int index) {
        NodeIncident temp = list.getFirst();
        int i = 0;
        while (temp != null && i < index) {
            temp = temp.getNext();
            i++;
        }
        return temp;
    }

    public static String printList(IncidentList list) {
        if (isEmpty(list)) {
            return "";
        }
        NodeIncident temp = list.getFirst();
        StringBuilder text = new StringBuilder();
        while (temp != null) {
            text.append(temp.getIncident().toString());
            if (temp.getNext() != null) {
                text.append("\n");
            }
            temp = temp.getNext();
        }
        return text.toString();
    }
}
