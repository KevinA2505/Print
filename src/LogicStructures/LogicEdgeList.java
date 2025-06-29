package LogicStructures;

import Nodes.Node;
import Nodes.NodeE;
import Structures.EdgeList;

public class LogicEdgeList {

    public static boolean isEmpty(EdgeList nodeList) {
        return nodeList.getFirst() == null;
    }

    public static void add(NodeE nodeE, EdgeList nodeList) {
        if (isEmpty(nodeList)) {
            Node newNode = new Node(nodeE);
            nodeList.setFirst(newNode);
            nodeList.setLast(newNode);
        } else {
            Node newNode = new Node(nodeE);
            nodeList.getLast().setNext(newNode);
            nodeList.setLast(newNode);
        }
    }

    public static void removeFirst(EdgeList nodeList) {
        if (isEmpty(nodeList)) {
            return;
        }
        
        Node temp = nodeList.getFirst();
        if (temp.getNext() == null) {
            nodeList.setFirst(null);
            nodeList.setLast(null);
        } else {
            nodeList.setFirst(temp.getNext());
        }
        temp.setNext(null);
    }

    public static int recurSize(Node head) {
        if (head == null) {
            return 0;
        }
        return 1 + recurSize(head.getNext());
    }

    public static void removeLast(EdgeList nodeList) {
        if (isEmpty(nodeList)) {
            return;
        }
        
        Node temp = nodeList.getFirst();
        
        // Si solo hay un elemento
        if (temp.getNext() == null) {
            nodeList.setFirst(null);
            nodeList.setLast(null);
            return;
        }
        
        // Buscar el penúltimo nodo
        while (temp.getNext() != null && temp.getNext() != nodeList.getLast()) {
            temp = temp.getNext();
        }
        
        if (temp.getNext() == nodeList.getLast()) {
            nodeList.setLast(temp);
            temp.setNext(null);
        }
    }

    public static int size(EdgeList nodeList) {
        Node temp = nodeList.getFirst();
        int size = 0;
        while (temp != null) {
            size++;
            temp = temp.getNext();
        }
        return size;
    }

    public static String printList(EdgeList nodeList) {
        if (isEmpty(nodeList)) {
            return "";
        }

        Node temp = nodeList.getFirst();
        StringBuilder text = new StringBuilder();

        while (temp != null) {
            text.append(temp.getNodeE().toString());
            if (temp.getNext() != null) {
                text.append(", ");
            }
            temp = temp.getNext();
        }
        
        return text.toString();
    }
}