package server;

import common.Constants;
import common.SocketChannelWrapper;
import contacts.Area;
import contacts.Contact;
import contacts.Entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class StreamingServer {
    private int idCounter = 0;
    private final int port;
    private boolean terminate = false;

    private final ForkJoinPool executor = (ForkJoinPool) Executors.newWorkStealingPool();
    //CachedThreadPool auch okay, weil undefinierte Menge, aber Threads wiederverwenden können
    private Selector selector;

    private volatile Area[] area;
    private Set<Contact> contactSet = new HashSet<>();
    private Hashtable<Integer, List<Integer>> entitiesInArea = new Hashtable<>();

    public StreamingServer (int port) {
        this.port = port;
    }

    public void startServer() {
        area = new Area(0, 0, Constants.SIZE, Constants.SIZE).split(Constants.SPLITS);

        System.out.println("Server starting on port " + port);
        try (final ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(port));
            System.out.println("Server listening on port " + port);

            executor.submit(this::killSwitchListener);
            System.out.println("Command Line KillSwitch registered -> You can use enter to kill the server");

            selector = Selector.open();
            server.configureBlocking(false);
            SelectionKey selectionKey = server.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(server);

            while (!terminate) {
                selector.select(100);
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                        SocketChannel channel = ssChannel.accept();
                        if (channel != null) {
                            System.out.println("Accepting a connection");
                            channel.configureBlocking(false);
                            SelectionKey attKey = channel.register(selector, SelectionKey.OP_READ);
                            attKey.attach(idCounter);
                            //Write id to client;
                            System.out.println("Write to client: " + idCounter);
                            channel.write(Constants.CSET.encode(String.valueOf(idCounter++)));
                        }
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        try (final SocketChannelWrapper wrapper = new SocketChannelWrapper(channel)) {
                            int id = (int) key.attachment();
                            int read = wrapper.readData();
                            if (read >= 0) {
                                String[] split = wrapper.getCharBuffer().toString().split(";");
                                Entity e1 = Entity.of(id, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                                System.out.println("Id:" + id + "; X:" + e1.getX() + "; Y:" + e1.getY());
                                executor.execute(new EntityHandler(e1, wrapper));
                            }
                        } catch (SocketException e) {
                            channel.shutdownOutput();
                        }
                    }
                    keyIterator.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void killSwitchListener() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!terminate) {
            try {
                if (reader.ready()) {
                    terminate = true;
                    return;
                }
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class EntityHandler implements Runnable {
        private final Entity entity;
        private final SocketChannelWrapper wrapper;
        private List<Integer> areaId;

        public EntityHandler (Entity entity, SocketChannelWrapper wrapper) {
            this.entity = entity;
            this.wrapper = wrapper;
        }

        @Override
        public void run() {
            synchronized (area) {
                areaId = entitiesInArea.get(entity.getId());
                if (areaId != null && areaId.size() > 0) {

                    for (int a : areaId) {
                        area[a].delete(entity);
                    }
                }

                int mainIndex = entity.getX() / (Constants.SIZE/Constants.SPLITS) + ((entity.getY() / (Constants.SIZE/Constants.SPLITS)) * Constants.SPLITS);
                areaId = new ArrayList<>();

                //Add main index
                addWithIndex(mainIndex);
                //Check left possible
                addWithIndex(mainIndex-1);
                //Check top
                addWithIndex(mainIndex-Constants.SPLITS);
                //Check right
                addWithIndex(mainIndex+1);
                //check bottom
                addWithIndex(mainIndex+Constants.SPLITS);
                //check diagonal left top
                addWithIndex(mainIndex-Constants.SPLITS-1);
                //check diagonal right top
                addWithIndex(mainIndex-Constants.SPLITS+1);
                //check diagonal left bottom
                addWithIndex(mainIndex+Constants.SPLITS-1);
                //check diagonal right bottom
                addWithIndex(mainIndex+Constants.SPLITS+1);
            }

            System.out.println("Checking for contacts now for: " + entity.getId());
            List<Contact> contacts = new ArrayList<>();
            for (int i : areaId) {
                synchronized (area[i]) {
                    ContactFinder contactFinder = new ContactFinder(area[i], Constants.TH_ENTITIES, Constants.TH_SIZE);
                    contacts.addAll(contactFinder.compute());
                }
            }

            for (Contact c : contacts) {
                boolean added;

                synchronized (contactSet) {
                    added = contactSet.add(c);
                }

                //Introduce the boolean to keep synchronized block small
                if (added) {
                    try {
                        wrapper.write(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            entitiesInArea.put(entity.getId(), areaId);
        }

        private void addWithIndex(int index) {
            if (index >= 0 && index < area.length && (area[index].isClose(entity) || area[index].isWithin(entity))) {
                areaId.add(index);
                synchronized (area[index]) {
                    area[index].add(entity);
                }
            }
        }
    }
}
