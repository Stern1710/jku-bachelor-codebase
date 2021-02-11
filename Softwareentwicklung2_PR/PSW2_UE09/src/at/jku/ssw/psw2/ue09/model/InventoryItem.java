package at.jku.ssw.psw2.ue09.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Represents an item in an {@link InventoryModel inventory system}. Each item provides a name, a more detailed description and how many instances of this item are currently in stock.
 */
@XmlRootElement
public final class InventoryItem implements Serializable {

    static final long serialVersionUID = -3387516993124229948L;

    private int id;
    private String name;
    private String description;
    private int quantity;

    public InventoryItem() {
        //Doing this like in the provided demo with calling the other constructor
        this(-1, null, null, -1);
    }

    public InventoryItem(int id, String name, String description, int quantity) {
        super();
        this.id = id;
        this.name = name;
        this.description = description == null ? "" : description;
        this.quantity = quantity;
    }

    /**
     * Returns a unique identifier for this item.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    @XmlElement
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Provides a short description / name for this item.
     *
     * @return the item's name
     */
    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Provides details about this item.
     *
     * @return a prose-text description of this item
     */
    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the number of instances of this product that are currently available.
     *
     * @return how much of this item is in stock
     */
    public int getQuantity() {
        return quantity;
    }

    @XmlElement
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof InventoryItem && id == ((InventoryItem) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
