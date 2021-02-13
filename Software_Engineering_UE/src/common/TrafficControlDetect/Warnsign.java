package common.TrafficControlDetect;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
/**
 * Represent a Warningsign Type
 * @author Christopher Holzweber
 *
 */
public enum Warnsign {
	WET(0), UNDERCONSTRUCTION(1), SPEEDLIMIT(2), NOSIGN(3);

	private int value;
	@SuppressWarnings("rawtypes")
	private static Map map = new HashMap<>();

	private Warnsign(int value) {
		this.value = value;
	}

	static {
		for (Warnsign signtypes : Warnsign.values()) {
			map.put(signtypes.value, signtypes);
		}
	}

	public static Warnsign valueOf(int signtype) {
		return (Warnsign) map.get(signtype);
	}

	public int getValue() {
		return value;
	}
}