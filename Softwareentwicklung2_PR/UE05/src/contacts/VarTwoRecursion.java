package contacts;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

import static contacts.Constants.CONTACT_DISTANCE;

public class VarTwoRecursion extends RecursiveTask<Set<Contact>> {
    private final Area area;
    private final int th_entities;
    private final int th_size;

    public VarTwoRecursion(Area area, int th_entities, int th_size) {
        this.area = area;
        this.th_entities = th_entities;
        this.th_size = th_size;
    }


    @Override
    protected Set<Contact> compute() {
        Set<Contact> contacts = new HashSet<>();

        if (area.getEntities().size() < th_entities || area.getWidth() < th_size) {
            //Sequentiel search, make it easy
            for (Entity e1: area.getEntities()) {
                for (Entity e2: area.getEntities()) {
                    if (!e1.equals(e2)) {
                        if (e1.distance(e2) < CONTACT_DISTANCE) {
                            contacts.add(Contact.of(e1, e2));
                        }
                    }
                }
            }
        } else {
            Area[] splittedAreas = area.split(2);

            VarTwoRecursion task1 = new VarTwoRecursion(splittedAreas[0], th_entities, th_size);
            VarTwoRecursion task2 = new VarTwoRecursion(splittedAreas[1], th_entities, th_size);
            VarTwoRecursion task3 = new VarTwoRecursion(splittedAreas[2], th_entities, th_size);
            VarTwoRecursion task4 = new VarTwoRecursion(splittedAreas[3], th_entities, th_size);

            task1.fork();
            task2.fork();
            task3.fork();
            task4.fork();

            contacts.addAll(task1.join());
            contacts.addAll(task2.join());
            contacts.addAll(task3.join());
            contacts.addAll(task4.join());
        }

        return contacts;
    }
}
