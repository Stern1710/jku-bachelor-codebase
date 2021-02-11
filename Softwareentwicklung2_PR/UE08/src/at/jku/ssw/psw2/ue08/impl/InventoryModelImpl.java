package at.jku.ssw.psw2.ue08.impl;

import at.jku.ssw.psw2.ue08.model.InventoryChangeListener;
import at.jku.ssw.psw2.ue08.model.InventoryItem;
import at.jku.ssw.psw2.ue08.model.InventoryModel;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class InventoryModelImpl extends UnicastRemoteObject implements InventoryModel<InventoryItemImpl> {

    private final Map<String, InventoryItemImpl> items;
    private final List<InventoryChangeListener<InventoryItemImpl>> listeners;

    private ExecutorService listenerExe = Executors.newFixedThreadPool(10);

    public InventoryModelImpl() throws RemoteException {
        //Using synchronized lists instead of synchronized blocks
        items = Collections.synchronizedMap(new HashMap<>());
        listeners = Collections.synchronizedList(new ArrayList<>());
    }

    private void fireItemAdded(InventoryItemImpl addedItem) {
        listeners.forEach(l -> {
            listenerExe.submit(() -> {
                try {
                    l.onItemAdded(addedItem);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void fireItemChanged(InventoryItemImpl changedItem) {
        listeners.forEach(l -> {
            listenerExe.submit(() -> {
                try {
                    l.onItemChanged(changedItem.getName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void fireItemRemoved(InventoryItemImpl removedItem) {
        listeners.forEach(l -> {
            listenerExe.submit(() -> {
                try {
                    l.onItemRemoved(removedItem);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public List<InventoryItemImpl> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    @Override
    public InventoryItemImpl getItem(String name) throws IllegalArgumentException, NoSuchElementException {
        if (name == null) {
            throw new IllegalArgumentException("Invalid name");
        }
        return items.get(name);
    }

    @Override
    public void createItem(String name) throws IllegalArgumentException, RemoteException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (items.get(name) != null) {
            throw new IllegalArgumentException("Duplicate item: " + name);
        }

        final InventoryItemImpl item = new InventoryItemImpl(name);
        items.put(name, item);
        fireItemAdded(item);
    }

    @Override
    public void setDescription(String itemName, String description) throws IllegalArgumentException {
        if (itemName == null || description == null) {
            throw new IllegalArgumentException("Invalid change");
        }
        InventoryItemImpl item = items.get(itemName);
        try {
            item.setDescription(description);
            fireItemChanged(item);
        } catch (NullPointerException error) {
            throw new NullPointerException("The item no longer exists / was deleted by another user");
        }

    }

    @Override
    public void increaseQuantity(String itemName, int increase) throws IllegalArgumentException {
        if (itemName == null) {
            throw new IllegalArgumentException("Invalid item to change");
        } else if (increase < 0) {
            throw new IllegalArgumentException("Invalid quantity increase: " + increase);
        } else if (increase == 0) {
            return;
        }
        InventoryItemImpl item = items.get(itemName);

        if (item == null) {
            throw new IllegalArgumentException("Invalid item to change");
        } else if (Integer.MAX_VALUE - item.getQuantity() < increase) {
            throw new IllegalArgumentException("Maximum quantity is restricted to " + Integer.MAX_VALUE);
        } else {
            try {
                item.setQuantity(item.getQuantity() + increase);
                fireItemChanged(item);
            } catch (NullPointerException error) {
                throw new NullPointerException("The item no longer exists / was deleted by another user");
            }
        }
    }

    @Override
    public void decreaseQuantity(String itemName, int decrease) throws IllegalArgumentException {
        if (decrease < 0) {
            throw new IllegalArgumentException("Invalid quantity decrease: " + decrease);
        } else if (decrease == 0) {
            return;
        }

        InventoryItemImpl item = items.get(itemName);

        if (item.getQuantity() < decrease) {
            throw new IllegalArgumentException("Minimum quantity is 0");
        } else {
            try {
                item.setQuantity(item.getQuantity() - decrease);
                fireItemChanged(item);
            } catch (NullPointerException error) {
                throw new NullPointerException("The item no longer exists / was deleted by another user");
            }
        }
    }

    @Override
    public void deleteItem(String itemName) throws IllegalArgumentException {
        InventoryItemImpl item = items.get(itemName);
        if (item == null) {
            return;
        }

        final boolean removed = items.remove(itemName, items.get(itemName));
        if (removed) {
            fireItemRemoved(item);
        }
    }

    @Override
    public void addListener(InventoryChangeListener<InventoryItemImpl> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(InventoryChangeListener<InventoryItemImpl> listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}
