package common.TrafficControlDetect;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
/**
 * Represent a StreetMode Type
 * @author Christopher Holzweber
 *
 */
public enum StreetMode {
    STANDBY(0), 
    NORMAL(1), 
    BLOCK(2),
    MAINTAINWARNING(3); 
    
    private int value;
	@SuppressWarnings("rawtypes")
	private static Map map = new HashMap<>();

	private StreetMode(int value) {
		this.value = value;
	}

	static {
		for (StreetMode modes : StreetMode.values()) {
			map.put(modes.value, modes);
		}
	}

	public static StreetMode valueOf(int light) {
		return (StreetMode) map.get(light);
	}

	public int getValue() {
		return value;
	}
}