package network;

import java.util.*;

import inout.Window;

public class TrafficNetwork {
	private SortedMap<String, Location> locations;
	private List<Link> links;

	public TrafficNetwork() {
		locations = new TreeMap<>();
		links = new ArrayList<>();
	}

	public void addLocation(Location loc) {
		locations.put(loc.getName(), loc);
	}

	public void addLocation(String name, int x, int y) {
		addLocation(new Location(name, x, y));
	}

	public Location getLocation(String name) {
		return locations.get(name);
	}

	public Collection<Location> getLocations() {
		return new LinkedList<>(locations.values());
	}

	public void addLink(String from, String to, String name, LinkType type, int length) {
		Link newLink = new Link(name, type, length, locations.get(from), locations.get(to));
		links.add(newLink);
		locations.get(from).addNewLink(newLink);
		locations.get(to).addNewLink(newLink);
	}

	public void draw() {
		Window.open();
		for (Link lnk : links) {
			lnk.draw();
		}
		for (Location loc : locations.values()) {
			loc.draw();
		}
	}

}
