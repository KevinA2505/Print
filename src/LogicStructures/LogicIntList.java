package LogicStructures;

import Nodes.NodeInt;
import Structures.IntList;

public class LogicIntList {

    public static boolean isEmpty(IntList list) {
        return list.getFirst() == null;
    }

    public static void add(int data, IntList list) {
        NodeInt newNode = new NodeInt(data);
        if (isEmpty(list)) {
            list.setFirst(newNode);
            list.setLast(newNode);
        } else {
            list.getLast().setNext(newNode);
            list.setLast(newNode);
        }
    }

    public static void removeFirst(IntList list) {
        if (isEmpty(list)) {
            return;
        }
        NodeInt temp = list.getFirst();
        if (temp.getNext() == null) {
            list.setFirst(null);
            list.setLast(null);
        } else {
            list.setFirst(temp.getNext());
        }
        temp.setNext(null);
    }

    public static void removeLast(IntList list) {
        if (isEmpty(list)) {
            return;
        }
        NodeInt temp = list.getFirst();
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

    public static int size(IntList list) {
        NodeInt temp = list.getFirst();
        int size = 0;
        while (temp != null) {
            size++;
            temp = temp.getNext();
        }
        return size;
    }

    public static NodeInt getAt(IntList list, int index) {
        NodeInt temp = list.getFirst();
        int i = 0;
        while (temp != null && i < index) {
            temp = temp.getNext();
            i++;
        }
        return temp;
    }

    public static String printList(IntList list) {
        if (isEmpty(list)) {
            return "";
        }
        NodeInt temp = list.getFirst();
        StringBuilder text = new StringBuilder();
        while (temp != null) {
            text.append(temp.getData());
            if (temp.getNext() != null) {
                text.append(", ");
            }
            temp = temp.getNext();
        }
        return text.toString();
    }
}
