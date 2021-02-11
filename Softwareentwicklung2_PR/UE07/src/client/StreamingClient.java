package client;

import common.SocketChannelWrapper;
import contacts.Contact;
import contacts.Entity;
import contacts.WalkUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class StreamingClient implements Runnable {
    private final String address;
    private final int port, x, y;
    private final Set<Contact> contacts;
    private int given_id = -1;
    private boolean terminate = false;

    public StreamingClient(String address, int port, int x, int y, Set<Contact> contacts) {
        this.address = address;
        this.port = port;
        this.x = x;
        this.y = y;
        this.contacts = contacts;
    }

    @Override
    public void run() {
        Entity entity = null;
        Thread receiver = null;

        try (SocketChannelWrapper wrapper = new SocketChannelWrapper(SocketChannel.open(new InetSocketAddress(address, port)))) {

            //Read the server passed id
            wrapper.readData();
            String ofCharBuffer = wrapper.getCharBuffer().toString();
            given_id = Integer.parseInt(ofCharBuffer);
            System.out.println("Read: " + given_id);
            entity = Entity.of(given_id, x, y);

            wrapper.write(entity.getX()+";"+entity.getY());

            receiver = new Thread(new Receiver(wrapper));
            receiver.start();

            while(!terminate) {
                WalkUtil.delay();
                entity = WalkUtil.walk(entity);
                wrapper.write(given_id+";"+entity.getX()+";"+entity.getY());
            }

            wrapper.close();
            System.out.println("Client " + given_id + ": Disconnected from server");
            //Terminate this thread

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Setting terminate to true");
            terminate = true;
        }
    }

    public void terminateMe() {
        this.terminate = true;
    }

    private class Receiver implements Runnable {
        private final SocketChannelWrapper wrapper;

        Receiver(SocketChannelWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void run() {
            Contact contact;
            while (!terminate) {
                try {
                    int read = wrapper.readData();
                    if (read >= 0) {
                        String[] split = wrapper.getCharBuffer().toString().split(";");

                        Entity e1 = Entity.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                        Entity e2 = Entity.of(Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
                        contact = Contact.of(e1, e2);

                        if (!contacts.contains(contact)) {
                            synchronized (contacts) {
                                contacts.add(contact);
                            }
                            System.out.println("Client " + given_id + ": Received a contact -> " + contact.toString());
                        }
                    }

                } catch (Exception e) {

                }
            }

            System.out.println("Client " + given_id + ": Stopped reading data");
        }
    }
}
