package domain;

public class Incident {

    private String type;       // "CHOQUE", "REPARACION", "CONGESTION"
    private int row;
    private int col;
    private String description;
    private Incident next;     // Enlace para lista enlazada

    public Incident(String type, int row, int col, String description) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.description = description;
        this.next = null;
    }

    public String getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getDescription() {
        return description;
    }

    public Incident getNext() {
        return next;
    }

    public void setNext(Incident next) {
        this.next = next;
    }

    public String getStreetName() {
        return "Calle " + row;
    }

    public String getAvenueName() {
        return "Avenida " + col;
    }

    public String toString() {
        return "[" + type + "] en (" + row + "," + col + ") - " + description;
    }
}
