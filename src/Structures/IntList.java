package Structures;

import Nodes.NodeInt;

public class IntList {
    private NodeInt first;
    private NodeInt last;

    public IntList() {
        this.first = null;
        this.last = null;
    }

    public NodeInt getFirst() {
        return first;
    }

    public void setFirst(NodeInt first) {
        this.first = first;
    }

    public NodeInt getLast() {
        return last;
    }

    public void setLast(NodeInt last) {
        this.last = last;
    }
}
