package at.jku.ssw.psw2.ue09.model;

/**
 * Class of exceptions thrown by an {@link InventoryModel}.
 */
public class InventoryException extends Exception {

    /**
     * This exception indicates that an item id sent in a request to the model does not exist.
     */
    public static final class NoSuchItem extends InventoryException {
        public NoSuchItem(int itemId) {
            super(itemId, "Invalid item");
        }
    }

    /**
     * If returned by {@link #getItemId()}, indicates that the according error is a general one and does not affect any specific item id
     */
    public static final int NO_SPECIFIC_ITEM = -1;

    private final int itemId;

    /**
     * Constructor.
     *
     * @param itemId  the id of the affected item, or {@link #NO_SPECIFIC_ITEM}
     * @param message custom error message
     * @param cause   additional details for error source
     */
    public InventoryException(int itemId, String message, Throwable cause) {
        super(message, cause);
        this.itemId = itemId;
    }

    /**
     * Constructor.
     *
     * @param itemId  the id of the affected item, or {@link #NO_SPECIFIC_ITEM}
     * @param message custom error message
     */
    public InventoryException(int itemId, String message) {
        super(message);
        this.itemId = itemId;
    }

    /**
     * Constructor.
     *
     * @param itemId the id of the affected item, or {@link #NO_SPECIFIC_ITEM}
     * @param cause  additional details for error source
     */
    public InventoryException(int itemId, Throwable cause) {
        super(cause);
        this.itemId = itemId;
    }

    /**
     * Constructor.
     *
     * @param itemId the id of the affected item, or {@link #NO_SPECIFIC_ITEM}
     */
    public InventoryException(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Constructor.
     *
     * @param message custom error message
     * @param cause   additional details for error source
     */
    public InventoryException(String message, Throwable cause) {
        this(NO_SPECIFIC_ITEM, message, cause);
    }

    /**
     * Constructor.
     *
     * @param message custom error message
     */
    public InventoryException(String message) {
        this(NO_SPECIFIC_ITEM, message);
    }

    /**
     * Constructor.
     *
     * @param cause additional details for error source
     */
    public InventoryException(Throwable cause) {
        this(NO_SPECIFIC_ITEM, cause);
    }

    /**
     * Identifies the item affected by this error. If {@link #NO_SPECIFIC_ITEM} is returned, this error is not item-specific or may afffect multiple items.
     *
     * @return the id of the affected item, or {@link #NO_SPECIFIC_ITEM}
     */
    public int getItemId() {
        return itemId;
    }
}
