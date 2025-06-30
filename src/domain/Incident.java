package domain;

/*
 * Representa un incidente ocurrido en la simulación, indicando el tipo, la
 * posición donde se genera y una breve descripción.
 */
public class Incident {

    private String type;
    private int row;
    private int col;
    private String description;
    private Incident next;

    /*
     * Crea un nuevo incidente en la ubicación indicada.
     */
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

    /*
     * Representación legible para la fila del incidente.
     */
    public String getStreetName() {
        return "Calle " + row;
    }

    /*
     * Representación legible para la columna del incidente.
     */
    public String getAvenueName() {
        return "Avenida " + col;
    }

    /*
     * Devuelve una representación legible del incidente para la consola o las
     * tablas de la interfaz.
     */
    public String toString() {
        return "[" + type + "] en (" + row + "," + col + ") - " + description;
    }
}
