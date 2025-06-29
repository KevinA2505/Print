package Nodes;

import domain.Car;

public class QueueNode {

	private Car car;
	private QueueNode next;

	public QueueNode() {
		this.car = null;
		this.next = null;
	}

	public QueueNode(Car car) {
		this.car = car;
		this.next = null;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public QueueNode getNext() {
		return next;
	}

	public void setNext(QueueNode next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return car != null ? car.toString() : "null";
	}
}