package common.Participant;

import java.util.HashMap;
import java.util.Map;

/**
 * The aim of this enum is to have the different kind of RoadTypes we can assign to our Participants.
 * Additionally the designated values are used so the control system can easily work with the enum types.
 * In the Hashmap we are the able to get the assigned speed limit of each Road.
 * @author Treml
 */

@SuppressWarnings("unchecked")
public enum Roads {
	
	HIGHWAY(0),
	INNERCITYROAD(1),
	OUTERCITYROAD(2),
	BIKELANE(3),
	SIDEWALK(4),
	BUSLANE(5),
	SUBWAYLANE(6);
	
    private int value;
	@SuppressWarnings("rawtypes")
	private static Map map = new HashMap<>();

	private Roads(int value) {
		this.value = value;
	}
	
	static {
		for (Roads modes : Roads.values()) {
			map.put(modes.value, modes);
		}
	}

	public static Roads valueOf(int limit) {
		return (Roads) map.get(limit);
	}

	public int getValue() {
		return value;
	}
}
