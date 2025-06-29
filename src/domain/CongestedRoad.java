package domain;

public class CongestedRoad {
    private int i;
    private int j;
    private int cars;

    public CongestedRoad(int i, int j, int cars) {
        this.i = i;
        this.j = j;
        this.cars = cars;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getCars() {
        return cars;
    }

    public void setCars(int cars) {
        this.cars = cars;
    }

    public String getCoord() {
        return "(" + i + "," + j + ")";
    }
}
