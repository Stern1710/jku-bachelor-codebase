package contacts;

import java.util.HashSet;
import java.util.Set;

import static contacts.Constants.*;
import static contacts.Constants.SIZE;

public class MainVarOne {
    public static void main(String[] args) {
        System.out.format("SIZE: %,d%n", SIZE);
        System.out.format("N_ENTITIES: %,d%n", N_ENTITIES);
        System.out.format("DISTANCE: %,d%n", CONTACT_DISTANCE);
        System.out.println("-------------------------------------------------");

        // creates an area and populates the area with entities
        long start;
        Set<Contact> contacts = new HashSet<Contact>();

        Area area = new Area(0, 0, SIZE, SIZE);
        populate(area, N_ENTITIES);

        start = System.currentTimeMillis();
        VarOneThreadPool parallel = new VarOneThreadPool(area.split(NUM_OF_SPLITS));
        contacts = parallel.findContactsParallel();

        print(contacts, "Parallel Method 1", start, System.currentTimeMillis());

        /* Findings for balanced settings for fixed areas and thread pool
         * Small settings: SIZE_OF_AREA = 16; ~310 to 340 ms on PC
         * Moderate settings: SIZE_OF_AREA = 32; ~2100 to 2200 ms on PC
         * Large settings: SIZE_OF_AREA = 64; ~ 276 000ms (4 Minutes 36 Seconds) on PC
         */
    }

    /**
     * Randomly populates an area with n entities.
     * @param area
     */
    private static void populate(Area area, int n) {
        for (int i = 0; i < n; i++) {
            Entity e = Entity.at(RAND.nextInt(SIZE), RAND.nextInt(SIZE));
            area.add(e);
        }

    }

    private static void print (Set<Contact> contacts, String descr, long timeStart, long timeEnd) {
        System.out.println("\n========== " + descr +" ========================");
        System.out.format("\nElapsed time [ms] %,d", (timeEnd - timeStart));

        System.out.println("\n-------------------------------------------------");
        System.out.println("Contacts: " + contacts.size());
        System.out.println("\n-------------------------------------------------");
        for (Contact c : contacts) {
            System.out.println(c);
        }
        System.out.println("\n-------------------------------------------------");
    }
}
