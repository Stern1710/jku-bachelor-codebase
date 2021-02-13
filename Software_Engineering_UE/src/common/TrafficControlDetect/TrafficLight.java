package common.TrafficControlDetect;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
/**
 * Represent a Trafficlight Type
 * @author Christopher Holzweber
 *
 */
public enum TrafficLight {
    RED(0),
    NORMAL(1),
    WARNING(2);
    
	private int value;
	@SuppressWarnings("rawtypes")
	private static Map map = new HashMap<>();

	private TrafficLight(int value) {
		this.value = value;
	}

	static {
		for (TrafficLight signtypes : TrafficLight.values()) {
			map.put(signtypes.value, signtypes);
		}
	}

	public static TrafficLight valueOf(int light) {
		return (TrafficLight) map.get(light);
	}

	public int getValue() {
		return value;
	}
}