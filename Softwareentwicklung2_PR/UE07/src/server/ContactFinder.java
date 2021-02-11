package server;

import contacts.Area;
import contacts.Contact;
import contacts.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import static common.Constants.CONTACT_DISTANCE;

public class ContactFinder extends RecursiveTask<List<Contact>> {

    private final Area area;
    private final int th_entities;
    private final int th_size;

    public ContactFinder(Area area, int th_entities, int th_size) {
        this.area = area;
        this.th_entities = th_entities;
        this.th_size = th_size;
    }

    @Override
    protected List<Contact> compute() {
        List<Contact> contacts = new ArrayList<>();

        if (area.getEntities().size() < th_entities || area.getWidth() < th_size) {
            //Sequential search, make it easy
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

            ContactFinder task1 = new ContactFinder(splittedAreas[0], th_entities, th_size);
            ContactFinder task2 = new ContactFinder(splittedAreas[1], th_entities, th_size);
            ContactFinder task3 = new ContactFinder(splittedAreas[2], th_entities, th_size);
            ContactFinder task4 = new ContactFinder(splittedAreas[3], th_entities, th_size);

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
