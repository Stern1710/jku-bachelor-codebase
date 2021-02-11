package network.test;

import java.util.Comparator;

import inout.In;
import inout.Out;
import network.*;

public class TrafficSystemTest {

	private static final TrafficNetwork ts = new TrafficNetwork();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		ts.addLocation("Linz", 300, 80);
		ts.addLocation("Sattledt", 230, 110);
		ts.addLocation("Steyermuehl", 180, 120);
		ts.addLocation("Gmunden", 175, 150);
		ts.addLocation("Ischl", 180, 176);
		ts.addLocation("Hallstatt", 172, 190);
		ts.addLocation("Salzburg", 100, 140);
		ts.addLocation("Freistadt", 310, 60);
		ts.addLocation("Wels", 240, 90);
		ts.addLocation("Kirchdorf", 250, 150);
		ts.addLocation("Rottenmann", 260, 240);
		ts.addLocation("Graz", 330, 360);

		ts.addLink("Linz", "Freistadt", "B310", LinkType.BUNDESSTRASSE, 45);
		ts.addLink("Linz", "Wels", "A9", LinkType.AUTOBAHN, 52);
		ts.addLink("Linz", "Sattledt", "A1", LinkType.AUTOBAHN, 58);
		ts.addLink("Sattledt", "Steyermuehl", "A1", LinkType.AUTOBAHN, 22);
		ts.addLink("Steyermuehl", "Salzburg", "A1", LinkType.AUTOBAHN, 100);
		ts.addLink("Steyermuehl", "Gmunden", "B145", LinkType.BUNDESSTRASSE, 26);
		ts.addLink("Gmunden", "Ischl", "B145", LinkType.BUNDESSTRASSE, 38);
		ts.addLink("Ischl", "Hallstatt", "B145", LinkType.LANDESSTRASSE, 24);
		ts.addLink("Wels", "Sattledt", "A8", LinkType.AUTOBAHN, 18);
		ts.addLink("Sattledt", "Kirchdorf", "A9", LinkType.AUTOBAHN, 45);
		ts.addLink("Kirchdorf", "Rottenmann", "A9", LinkType.AUTOBAHN, 65);
		ts.addLink("Rottenmann", "Graz", "A9", LinkType.AUTOBAHN, 107);
		ts.addLink("Rottenmann", "Ischl", "B145", LinkType.BUNDESSTRASSE, 107);
		ts.addLink("Ischl", "Salzburg", "B158", LinkType.BUNDESSTRASSE, 112);

		ts.draw();
		drive("Linz", new LinkLengthComparator());

	}

	private static void drive(final String start,
			final Comparator<Link> comparator) {

		Out.println();
		Out.println("================================================");
		Out.println(" Roundtrip starting at " + start);
		Out.println("================================================");

		final Location startLoc = ts.getLocation(start);
		Location current = startLoc;

		do {
			Out.println("You are currently in " + current.toString());
			// TODO
			Out.println("From " + current.getName() + " you can go  ");
			for (final Link lnk : current.getLinksSorted(comparator)) {
				Out.println("  " + lnk.toString() + " to "
						+ lnk.getOtherLocation(current).getName());
			}
			Out.println();
			final Location next = readNextLocation(current);
			if (next == null) {
				Out.println("Not readable --> End");
			} else {
				current = next;
			}
			Out.println();
		} while (current != null && current != startLoc);
		Out.println("Your are back! in " + current.getName());
		Out.println();
		Out.println("================================================");
	}

	private static Location readNextLocation(final Location current) {
		Out.print(" Next location: ");
		String next = In.readWord();
		Location nextLoc = ts.getLocation(next);
		while (nextLoc == null || !current.isNeighbor(nextLoc)) {
			Out.print(" Wrong location! Repeat next location: ");
			next = In.readWord();
			nextLoc = ts.getLocation(next);
		}
		return nextLoc;
	}

}
