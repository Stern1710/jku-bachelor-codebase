package at.jku.ssw.psw2.ue08.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Listener that observes changes to an {@link InventoryModel}.
 *
 * @param <ItemClass> the implementation class of items in the observed model
 */
public interface InventoryChangeListener<ItemClass extends InventoryItem> extends Remote {

    /**
     * Actions to be performed each time a new item is added to the observed model.
     *
     * @param addedItem the item that was newly added to the model
     */
    void onItemAdded(ItemClass addedItem) throws RemoteException;

    /**
     * Actions to be performed each time an item in the observed model is changed.
     *
     * @param itemName the item whose properties were changed
     */
    void onItemChanged(String itemName) throws RemoteException;

    /**
     * Actions to be performed each time an item is removed from the observed model.
     *
     * @param removedItem the item that was removed from the model
     */
    void onItemRemoved(ItemClass removedItem) throws RemoteException;
}
