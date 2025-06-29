package Nodes;

import LogicStructures.LogicTrafficLightList;
import Structures.EdgeList;
import Structures.Queue;
import Structures.RoadList;
import Structures.TrafficLightList;
import domain.TrafficLightView;

public class NodeV {

    private int data;
    private Queue cars;
    private RoadList xRoads;
    private RoadList yRoads;
    private EdgeList edges;

    private TrafficLightList trafficLights;
    private TrafficLightView trafficLightView;

    public NodeV(int data) {
        this.data = data;
        this.cars = new Queue();
        this.xRoads = new RoadList(data);
        this.yRoads = new RoadList(data);
        this.edges = new EdgeList();
        this.trafficLights = new TrafficLightList();
        LogicTrafficLightList.add(trafficLights, "X", true);
        LogicTrafficLightList.add(trafficLights, "Y", false);
//        this.trafficLights.add(new Nodes.NodeTrafficLight("X", true));
//        this.trafficLights.add(new Nodes.NodeTrafficLight("Y", false));
        this.trafficLightView = null;
    }

    public int getData() { return data; }
    public void setData(int data) { this.data = data; }
    public Queue getCars() { return cars; }
    public void setCars(Queue cars) { this.cars = cars; }
    public RoadList getxRoads() { return xRoads; }
    public void setxRoads(RoadList xRoads) { this.xRoads = xRoads; }
    public RoadList getyRoads() { return yRoads; }
    public void setyRoads(RoadList yRoads) { this.yRoads = yRoads; }
    public EdgeList getEdges() { return edges; }
    public void setEdges(EdgeList edges) { this.edges = edges; }

    public TrafficLightList getTrafficLights() { return trafficLights; }
    public void setTrafficLights(TrafficLightList trafficLights) { this.trafficLights = trafficLights; }

    public TrafficLightView getTrafficLightView() { return trafficLightView; }
    public void setTrafficLightView(TrafficLightView trafficLightView) { this.trafficLightView = trafficLightView; }

    @Override
    public String toString() {
        return data + "";
    }
}
