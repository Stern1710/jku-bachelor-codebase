package network;

import java.util.Comparator;

public class LinkLengthComparator implements Comparator<Link> {
    @Override
    public int compare(Link o1, Link o2) {
        return o1.getLength() - o2.getLength();
    }
}
