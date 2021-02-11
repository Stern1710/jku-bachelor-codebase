package client;

import contacts.Constants;
import contacts.Contact;

import java.util.*;

public class ClientMain {

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        Set<Contact> contactSet = Collections.synchronizedSet(new HashSet<>());
        Scanner scanner = new Scanner(System.in);

        threads.add(new Thread(new Client(Constants.SERVER_ADDR, Constants.PORT, 0, 0, contactSet)));
        threads.add(new Thread(new Client(Constants.SERVER_ADDR, Constants.PORT, 15, 0, contactSet)));
        threads.add(new Thread(new Client(Constants.SERVER_ADDR, Constants.PORT, 15, 15, contactSet)));
        threads.add(new Thread(new Client(Constants.SERVER_ADDR, Constants.PORT, 0, 15, contactSet)));

        System.out.println("Starting clients\n");
        for (Thread t: threads) {
            t.start();
        }

        //Wait for the user to enter something on the console that ends with a enter
        scanner.nextLine();

        System.out.println("Stopping clients");
        for (Thread t : threads) {
            t.interrupt();
        }

        print(contactSet, "All contacts");
    }

    private static void print (Set<Contact> contacts, String descr) {
        System.out.println("\n========== " + descr +" ===================");
        System.out.println("Contacts: " + contacts.size());
        System.out.println("\n-------------------------------------------------");
        for (Contact c : contacts) {
            System.out.println(c);
        }
        System.out.println("\n-------------------------------------------------");
    }
}
