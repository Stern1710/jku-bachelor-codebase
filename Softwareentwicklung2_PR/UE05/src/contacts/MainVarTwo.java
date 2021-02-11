package contacts;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import static contacts.Constants.*;
import static contacts.Constants.SIZE;

public class MainVarTwo {
    public static void main(String[] args) {
        System.out.format("SIZE: %,d%n", SIZE);
        System.out.format("N_ENTITIES: %,d%n", N_ENTITIES);
        System.out.format("DISTANCE: %,d%n", CONTACT_DISTANCE);
        System.out.println("-------------------------------------------------");

        // creates an area and populates the area with entities
        long start;
        Set<Contact> contacts;

        Area area = new Area(0, 0, SIZE, SIZE);
        populate(area, N_ENTITIES);

        start = System.currentTimeMillis();
        VarTwoRecursion recursion = new VarTwoRecursion(area, TH_ENTITIES, TH_SIZE);
        contacts = ForkJoinPool.commonPool().invoke(recursion);

        print(contacts, "Recursive Method 1", start, System.currentTimeMillis());

        /* Findings for balanced settings for recursive task and fork-join-pool
         * Small settings: TH_SIZE=64, TH_ENTITIES=128; ~ 320 ms on PC
         * Moderate settings: TH_SIZE=64 , TH_ENTITIES=128; ~ 360 - 420 ms on PC
         * Large settings: H_SIZE=64 , TH_ENTITIES=128; ~ 3600 - 3700 ms on PC
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
        System.out.println("\n========== " + descr +" ===================");
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
