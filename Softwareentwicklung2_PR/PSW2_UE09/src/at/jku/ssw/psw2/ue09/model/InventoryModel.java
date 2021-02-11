package at.jku.ssw.psw2.ue09.model;

import java.util.List;

/**
 * This class describes a data model for a simple inventory system. It manages different kinds
 * of {@link InventoryItem items} and the quantity available of each item.
 */
public interface InventoryModel extends AutoCloseable {

    /**
     * Initializes this model and ensures that all backing data sources are connected to.
     *
     * @throws InventoryException if a data source is inaccessible
     */
    void open() throws InventoryException;

    /**
     * Returns a read-only list of all items in the model.
     *
     * @return the items
     * @throws InventoryException if an error occurs during item retrieval
     */
    List<InventoryItem> getItems() throws InventoryException;

    /**
     * Adds a new kind of item to the inventory system.
     *
     * @param name the name of the item to be added
     * @return the id of the newly added item
     * @throws InventoryException if the name is null or empty or no new item could be created
     */
    int createItem(String name) throws InventoryException;

    /**
     * Returns the item with the given name.
     *
     * @param itemId the id of the item to retrieve
     * @return the item
     * @throws InventoryException if the referenced item does not exist in the model
     */
    InventoryItem getItem(int itemId) throws InventoryException;

    /**
     * Changes the {@link InventoryItem#getDescription() description} of this item.
     *
     * @param itemId      the id of the item to modify
     * @param description the new description
     * @throws InventoryException if the new description is null or if the referenced item does not exist in the model
     */
    void setDescription(int itemId, String description) throws InventoryException;

    /**
     * Decreases the number of stocked instances of this item.
     *
     * @param itemId     the id of the item whose stock to decrease
     * @param difference the change from the current quantity
     * @throws InventoryException if the referenced item does not exist in the model or the change cannot be performed
     */
    void changeQuantity(int itemId, int difference) throws InventoryException;

    /**
     * Deletes the given item from the inventory system.
     *
     * @param itemId the id of the item to be removed
     * @throws InventoryException if the referenced item does not exist in the model
     */
    void deleteItem(int itemId) throws InventoryException;

    /**
     * Severs any connection the model may have to backing data structures.
     *
     * @throws InventoryException if an error occurred during closing
     */
    @Override
    void close() throws InventoryException;
}
