package LogicStructures;

import Nodes.NodeTrafficLight;
import Structures.TrafficLightList;

public class LogicTrafficLightList {

	public static boolean isEmpty(TrafficLightList list) {
		return list.getFirst() == null;
	}

	public static void add(TrafficLightList list, String direction, boolean isGreen) {
		if (isEmpty(list)) {
			NodeTrafficLight newNode = new NodeTrafficLight(direction, isGreen);
			list.setFirst(newNode);
			list.setLast(newNode);
			return;
		}
		list.getLast().setNext(new NodeTrafficLight(direction, isGreen));
		list.setLast(list.getLast().getNext());
	}

	public static void toggleLights(TrafficLightList list) {
		NodeTrafficLight current = list.getFirst();
		while (current != null) {
			current.setGreen(!current.isGreen());
			current = current.getNext();
		}
	}

	public static String printList(TrafficLightList list) {
		String result = "";
		NodeTrafficLight current = list.getFirst();
		while (current != null) {
			result += current.toString() + " | ";
			current = current.getNext();
		}
		return result;
	}
}
