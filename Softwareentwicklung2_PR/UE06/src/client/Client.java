package client;

import contacts.Contact;
import contacts.Entity;
import contacts.WalkUtil;

import java.io.*;
import java.net.Socket;

import java.util.Set;

public class Client implements Runnable {
    private final String address;
    private final int port, x, y;
    private final Set<Contact> contacts;
    private int server_given_id = -1;

    public Client(String address, int port, int x, int y, Set<Contact> contacts) {
        this.address = address;
        this.port = port;
        this.x = x;
        this.y = y;
        this.contacts = contacts;
    }

    @Override
    public void run() {
        Socket socket = null;
        PrintWriter out = null;
        ObjectInputStream in = null;
        Entity entity = null;
        Thread receiverThread = null;

        //Connect to server
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get id from server and create entity
        try {
            server_given_id = (Integer) in.readObject();

            //Create entity and give starting position to server
            entity = Entity.of(server_given_id, x, y);
            out.println(entity.getX() + ";" + entity.getY());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        //Start receiver thread
        receiverThread = new Thread(new Receiver(in));
        receiverThread.start();

        System.out.println("Client " + server_given_id + ": Connected to server");

        //Run the thread as long as it isn't interrupted
        while (!Thread.interrupted()) {
            entity = WalkUtil.walk(entity);
            out.println(entity.getX() + ";" + entity.getY());
            WalkUtil.delay();
        }

        //End the receiver thread
        receiverThread.interrupt();

        //Disconnect from server
        out.println("terminate");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        System.out.println("Client " + server_given_id + ": Disconnected from server");
        //Terminate this thread
    }

    private class Receiver implements Runnable {
        private final ObjectInputStream in;

        Receiver(ObjectInputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            Contact contact;
            while (!Thread.interrupted()) {
                try {
                    contact = (Contact) in.readObject();
                    if (!contacts.contains(contact)) {
                        synchronized (contacts) {
                            contacts.add(contact);
                        }
                        System.out.println("Client " + server_given_id + ": Received a contact -> " + contact.toString());
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Client " + server_given_id + ": Stopped reading data");
        }
    }
}
