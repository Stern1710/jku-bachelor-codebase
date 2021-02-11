package network;

import inout.Window;

public class Link implements Comparable<Link> {

	private final String streetDesc;
	private final LinkType type;
	private final int length;
	private final Location start, end;

	public Link (String streetDesc, LinkType type, int length, Location start, Location end) {
		this.streetDesc = streetDesc;
		this.type = type;
		this.length = length;
		this.start = start;
		this.end = end;
	}

	//Calc with length / velocity instead of length*velocity as it was stated in the description is correct
	int getDrivingTime() {
		return length / type.getVelocity();
	}

	Location[] getEndPoints() {
		return new Location[] {start, end};
	}

	public Location getOtherLocation(Location loc) {
		return start.equals(loc) ? end:start;
	}

	int getLength() {
		return length;
	}

	public void draw() {
		Window.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
	}

	@Override
	public int compareTo(Link o) {
		int c = this.length - o.length;
		if (c == 0) {
			c = this.type.compareTo(o.type);
			if (c == 0) {
				c = this.streetDesc.compareTo(o.streetDesc);
			}
		}
		return c;
	}

	@Override
	public String toString() {
		return type.toString() + " " + streetDesc + ": " + length + " km";
	}
}
