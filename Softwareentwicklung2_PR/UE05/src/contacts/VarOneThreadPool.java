package contacts;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static contacts.Constants.CONTACT_DISTANCE;

public class VarOneThreadPool {
    private ExecutorService executor = Executors.newFixedThreadPool(100);

    private final Set<Contact> contacts = new HashSet<>();    //Shared ressource to save found contacts into
    private CountDownLatch endLatch;

    private final Area[] areas;
    private final int parallelism;

    public VarOneThreadPool(Area[] areas) {
        this.areas = areas;
        this.parallelism = areas.length;
    }

    public Set<Contact> findContactsParallel() {
        endLatch = new CountDownLatch(parallelism);

        for (int i=0; i < parallelism; i++) {
            executor.submit(new ContactRunnable(areas[i]));
        }

        try {
            endLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        return contacts;
    }

    private class ContactRunnable implements Runnable {

        private final Area area;

        public ContactRunnable (Area area) {
            this.area = area;
        }

        @Override
        public void run() {
            for (Entity e1: area.getEntities()) {
                for (Entity e2: area.getEntities()) {
                    if (!e1.equals(e2)) {
                        if (e1.distance(e2) < CONTACT_DISTANCE) {
                            Contact c = Contact.of(e1, e2);
                            synchronized (contacts) {
                                contacts.add(c);
                            }
                        }
                    }
                }
            }
            endLatch.countDown();
        }
    }
}
