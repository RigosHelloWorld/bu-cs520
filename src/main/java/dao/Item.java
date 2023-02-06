package main.java.dao;

import java.util.Objects;

/**
 * Item class represents the items that will be stored in the warehouse. 
 * @author Rigoberto Rosa
 */
public class Item {
    String upcCode;
    String description;
    int hashCode;


    /**
     * Constructs an item using a upcode and a description.
     * @param upcCode for this item
     * @param description for this item
     */
    public Item(String upcCode, String description){
        this.upcCode = upcCode;
        this.description = description;
        this.hashCode = Objects.hash(this.upcCode,this.description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Item tempItem = (Item) o;
        return upcCode.equals(tempItem.upcCode) ;
    }

    /**
     * Gets description. 
     * @return {@link Item#description}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets upcCode.
     * @return {@link Item#upcCode}
     */
    public String getUpcCode() {
        return upcCode;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Sets the description
     * @param description description of item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets upcCode.
     * @param upcCode upcCode of item
     */
    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    @Override
    public String toString() {
        return this.upcCode + ", " + this.description;
    }
}
