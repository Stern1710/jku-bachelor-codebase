package network;

import java.util.Comparator;

public class LinkTimeComparator implements Comparator<Link> {
    @Override
    public int compare(Link o1, Link o2) {
        return o1.getDrivingTime() - o2.getDrivingTime();
    }
}
