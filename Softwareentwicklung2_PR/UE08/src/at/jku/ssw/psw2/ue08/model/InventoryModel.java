package at.jku.ssw.psw2.ue08.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class describes a data model for a simple inventory system. It manages different kinds
 * of {@link ItemClass items} and the quantity available of each item. Changes to this model can be
 * observed using appropriate {@link InventoryChangeListener change listeners}.
 *
 * @param <ItemClass> type of items managed by this model
 */
public interface InventoryModel<ItemClass extends InventoryItem> extends Remote {

    /**
     * Returns a read-only list of all items in the model.
     *
     * @return the items
     */
    List<ItemClass> getItems() throws RemoteException;

    /**
     * Adds a new kind of item to the inventory system.
     *
     * @param name the name of the item to be added
     * @throws IllegalArgumentException if the given name is null or the model already contains an item with this name
     */
    void createItem(String name) throws IllegalArgumentException, RemoteException;

    /**
     * Returns the item with the given name.
     *
     * @param name the name of the item to retrieve
     *
     * @return the item
     *
     * @throws IllegalArgumentException if the name is null
     * @throws NoSuchElementException if there is no item with that name in the model
     */
    ItemClass getItem(String name) throws IllegalArgumentException, NoSuchElementException, RemoteException;

    /**
     * Changes the {@link InventoryItem#getDescription() description} of this item.
     *
     * @param itemName the name of the item to modify its description
     * @param description the new description
     * @throws IllegalArgumentException if the new description is null
     */
    void setDescription(String itemName, String description) throws IllegalArgumentException, RemoteException;

    /**
     * Increases the number of stocked instances of this item.
     *
     * @param itemName the name of the item to increase the quantity
     * @param increase the number of added instances
     * @throws IllegalArgumentException if the increase is negative or the increase would exceed the capacity of the store
     */
    void increaseQuantity(String itemName, int increase) throws IllegalArgumentException, RemoteException;

    /**
     * Decreases the number of stocked instances of this item.
     *
     * @param itemName the name of the item to decrease the quantity
     * @param decrease the number of removed instances
     * @throws IllegalArgumentException if the decrease is negative or higher than the number of currently stocked items
     */
    void decreaseQuantity(String itemName, int decrease) throws IllegalArgumentException, RemoteException;

    /**
     * Deletes the given item from the inventory system.
     *
     * @param itemName the name of the item to be removed
     * @throws IllegalArgumentException if the given item is null
     */
    void deleteItem(String itemName) throws IllegalArgumentException, RemoteException;

    /**
     * Adds a listener that is invoked when items are added to or removed from this model.
     *
     * @param listener the listener to add
     */
    void addListener(InventoryChangeListener<ItemClass> listener) throws RemoteException;

    /**
     * Removes the given listener from this item, if it is currently registered.
     *
     * @param listener the listener to remove
     */
    void removeListener(InventoryChangeListener<ItemClass> listener) throws RemoteException;
}
