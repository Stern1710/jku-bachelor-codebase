package server;

import contacts.Area;
import contacts.Contact;
import contacts.Entity;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static contacts.Constants.*;

public class Server implements Runnable {
    private int idCounter = 0;
    private final int port;

    private ServerSocket sSocket;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    private volatile Area[] area;
    private Set<Contact> contactSet = new HashSet<>();

    public Server (int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try{
            sSocket = new ServerSocket(port);
            sSocket.setSoTimeout(10000);

            area = new Area(0, 0, SIZE, SIZE).split(SPLITS);

            System.out.println("Server ready on port " + port + "\n");

            while (!Thread.interrupted()) {
                Socket socket = sSocket.accept();
                executor.submit(new ClientHandler(idCounter++, socket));
                System.out.println("Accepted connection: Handler " +idCounter);
            }

        } catch (SocketTimeoutException e) {
            System.out.println("\nServer not accepting new connections anymore due to timeout\n");
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sSocket != null) {
                terminate();
            }

        }
    }

    private void terminate() {
        executor.shutdownNow();
        try {
            sSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final int id;
        private final Socket socket;
        private final Entity entity;

        private int affectedCounter = 0;
        int[] affectedIndex = new int[9];

        public ClientHandler (int id, Socket socket) {
            this.id = id;
            this.socket = socket;
            entity = Entity.of(id, -1, -1);
        }

        @Override
        public void run() {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                String read;

                //First send the ID to the client
                out.writeObject(id);
                out.reset();

                //Receive coordinates for Entity and then update the entities position
                while ((read = in.readLine()) != null && !read.contains("terminate")) {
                    String[] split = read.split(";");
                    entity.goTo(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

                    //Do cool logic handling for updating the zone where the entity is

                    //Delete all entities from area
                    synchronized (area) {
                        if (affectedCounter > 0) {
                            //Area is different --> Delete from all areas
                            for(int i=0; i < affectedCounter - 1; i++) {
                                area[affectedIndex[i-1]].delete(entity);
                            }
                            affectedCounter = 0;
                        }

                        //Insert entity into affected areas
                        int mainIndex = entity.getX() / (SIZE/SPLITS) + ((entity.getY() / (SIZE/SPLITS)) * SPLITS);
                        affectedIndex[affectedCounter++] = mainIndex;
                        area[mainIndex].add(entity);

                        //Check left possible
                        addWithIndex(mainIndex-1);
                        //Check top
                        addWithIndex(mainIndex-SPLITS);
                        //Check right
                        addWithIndex(mainIndex+1);
                        //check bottom
                        addWithIndex(mainIndex+SPLITS);
                        //check diagonal left top
                        addWithIndex(mainIndex-SPLITS-1);
                        //check diagonal right top
                        addWithIndex(mainIndex-SPLITS+1);
                        //check diagonal left bottom
                        addWithIndex(mainIndex+SPLITS-1);
                        //check diagonal right bottom
                        addWithIndex(mainIndex+SPLITS+1);
                    }

                    //Recalculate the contacts for the given zone(s) (current, left?, top?)
                    List<Contact> contacts = new ArrayList<>();
                    for (int i=0; i < affectedCounter; i++) {
                        synchronized (area[affectedIndex[i]]) {
                            ContactFinder contactFinder = new ContactFinder(area[affectedIndex[i]], TH_ENTITIES, TH_SIZE);
                            contacts.addAll(contactFinder.compute());
                        }
                    }

                    for (Contact c : contacts) {
                        boolean added = false;

                        synchronized (contactSet) {
                            added = contactSet.add(c);
                        }

                        //Introduce the boolean to keep synchronized block small
                        if (added) {
                            out.writeObject(c);
                            out.reset();
                        }

                    }
                }

                //End connection --> Close all open streams and the socket
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Stopping ClientHandler " + id);
        }

        private void addWithIndex(int index) {
            if (index >= 0 && index < area.length && (area[index].isClose(entity) || area[index].isWithin(entity))) {
                affectedIndex[affectedCounter++] = index;
                synchronized (area[index]) {
                    area[index].add(entity);
                }
            }
        }
    }
}
