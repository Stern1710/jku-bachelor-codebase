package at.jku.ssw.psw2.ue08.impl;

import at.jku.ssw.psw2.ue08.model.InventoryItem;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public final class InventoryItemImpl extends UnicastRemoteObject implements InventoryItem {

    private final String name;

    private String description;
    private int quantity;

    public InventoryItemImpl(String name) throws RemoteException {
        super();
        this.name = name;
        this.description = "";
        this.quantity = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /*
     * This method is called to get the string displayed in the gui's list view of available items. We override it to display the item's name in the list.
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
