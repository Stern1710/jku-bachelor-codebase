package client;

import common.Constants;
import contacts.Contact;

import java.util.*;

public class MainClient {
    public static void main(String[] args) {
        List<StreamingClient> clients = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        Set<Contact> contacts = new HashSet<>();
        Scanner scanner = new Scanner(System.in);

        clients.add(new StreamingClient(Constants.SERVER_ADDR, Constants.PORT, 0, 0, contacts));
        clients.add(new StreamingClient(Constants.SERVER_ADDR, Constants.PORT, 10, 10, contacts));
        clients.add(new StreamingClient(Constants.SERVER_ADDR, Constants.PORT, 0, 10, contacts));
        clients.add(new StreamingClient(Constants.SERVER_ADDR, Constants.PORT, 10, 0, contacts));

        for (StreamingClient sc : clients) {
            threads.add(new Thread(sc));
        }

        for (Thread t: threads) {
            t.start();
        }

        System.out.println("Started clients\n");
        //Wait for the user to enter something on the console that ends with a enter
        scanner.nextLine();

        System.out.println("Stopping clients");
        for (StreamingClient sc : clients) {
            sc.terminateMe();
        }
        for (Thread t : threads) {
            t.currentThread().interrupt();
        }

        print(contacts, "All contacts");
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
