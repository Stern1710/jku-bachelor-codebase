package network;

import inout.Window;

import java.util.*;
import java.util.stream.Collectors;

public class Location implements Comparable<Location> {
	private final String name;
	private final int x, y;
	private final List<Link> outgoing;

	public Location(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		outgoing = new ArrayList<>();
	}

	public void draw() {
		Window.drawText(name, x, y);
	}

	public String getName() {
		return name;
	}

	public boolean isNeighbor(Location nextLoc) {
		return outgoing.stream().anyMatch(x -> x.getOtherLocation(this).equals(nextLoc));
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	void addNewLink (Link link) {
		outgoing.add(link);
	}

	Collection<Link> getLinks() {
		return outgoing;
	}

	Collection<Location> getNeighbors() {
		return outgoing.stream()
				.map(x -> x.getOtherLocation(this))
				.collect(Collectors.toList());
	}

	Location getNeighborFor(Link lnk) {
		return outgoing.stream()
				.filter(x -> x.equals(lnk))
				.findFirst()
				.orElse(null)
				.getOtherLocation(this);
	}

	Link getLinkTo(Location neighbor) {
		return outgoing.stream()
				.filter(x -> x.getOtherLocation(this).equals(neighbor))
				.findFirst().orElse(null);
	}

	public List<Link> getLinksSorted(Comparator<Link> comparator) {
		return outgoing.stream()
				.sorted(comparator)
				.collect(Collectors.toList());
	}

	List<Location> getNeighborsSorted(Comparator<Link> comparator) {
		return outgoing.stream()
				.sorted(comparator)
				.map(x -> x.getOtherLocation(this))
				.collect(Collectors.toList());
	}

	@Override
	public int compareTo(Location o) {
		int c = this.x - o.x;
		if (c == 0) {
			c = this.y - o.x;
			if (c == 0) {
				c = this.name.compareTo(o.name);
			}
		}
		return c;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return x == location.x && y == location.y && Objects.equals(name, location.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, x, y);
	}

	@Override
	public String toString() {
		return name + " (" + x + "/" + y + ")";
	}
}
