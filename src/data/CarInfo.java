package data;

public class CarInfo {
    private int id;
    private int origin;
    private int destination;

    public CarInfo() {}

    public CarInfo(int id, int origin, int destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
}
