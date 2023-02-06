package main.java.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.java.constants.FileConstants;
import main.java.dao.Item;
import main.java.exceptions.WarehouseFullException;

/**
 * TotesList class holds a list of totes.
 * 
 * @author Rigoberto Rosa
 */
public class TotesList {

    public static int numberOfTotesCreated = 0;
    List<Tote> totesList;
    private int numberOfFullTotes = 0;
    private int numberOfPartialTotes = 0;

    private int itemQuantities = 0;

    protected TotesList() {
        totesList = new ArrayList<>();
    }

    /**
     * Adds a new item to the next available tote.
     * 
     * @param item to be added to a tote
     * @return true if a new tote gets created false otherwise.
     * @throws WarehouseFullException if there is no more available totes
     */
    public boolean add(Item item) throws WarehouseFullException {
        if (totesList.size() == 0 || totesList.get(totesList.size() - 1).isFull()) {
            totesList.add(createTote(item));
            numberOfPartialTotes++;
            itemQuantities++;
            return true;
        }

        addItem(item, totesList.size() - 1);
        return false;
    }

    /**
     * Adds a an item to the totes list at the specific list position.
     * 
     * @param item         item to be added to the totes list.
     * @param listPosition the location in the list to add the item.
     */
    private void addItem(Item item, int listPosition) {
        totesList.get(listPosition).addItem(item);
        itemQuantities++;
        // Check to see if the tote is now full after adding the item
        if (totesList.get(listPosition).isFull()) {
            numberOfFullTotes++;
            numberOfPartialTotes--;
        }
    }

    /**
     * If the warehouse is not full creates a new tote and adds the item to it.
     * 
     * @param item to be added to the newly created tote
     * @return the tote that has been created
     * @throws WarehouseFullException if there is no more available totes
     */
    private Tote createTote(Item item) throws WarehouseFullException {
        isWarehouseFull();
        Tote tote = new Tote(numberOfTotesCreated);
        tote.addItem(item);
        return tote;
    }

    /**
     * Returns the number of items in the list.
     * 
     * @return int number of items
     */
    public int getItemQuantities() {
        return itemQuantities;
    }

    /**
     * Returns the number of full totes.
     * 
     * @return int number of full totes.
     */
    public int getNumOfFullTotes() {
        return numberOfFullTotes;
    }

    /**
     * Returns the number of partially filled totes.
     * 
     * @return int number of partially filled totes.
     */
    public int getNumOfPartialTotes() {
        return numberOfPartialTotes;
    }

    /**
     * Returns the total totes in this list (total = fullTotes +
     * partitallyFilledTotes).
     * 
     * @return int (number of full totes + number of partially filled totes)
     */
    public int getTotalTotes() {
        return numberOfFullTotes + numberOfPartialTotes;
    }

    /**
     * Checks if a tote is currently full.
     * 
     * @param listPosition the position in the list of totes.
     * @return true if the tote is full false otherwise.
     */
    private boolean isToteCurrentlyFull(int listPosition) {
        return totesList.get(listPosition).isFull();
    }

    /**
     * Checks if we can create an additional tote.
     * 
     * @throws WarehouseFullException if there is no more available totes
     */
    private void isWarehouseFull() throws WarehouseFullException {
        if (++numberOfTotesCreated >= FileConstants.TOTAL_TOTES_IN_WAREHOUSE) {
            numberOfTotesCreated--;
            throw new WarehouseFullException("Warehouse is full:");
        }
    }

    /**
     * Tries to merge any unfilled totes using a sliding window approach. 
     * @return true if any totes were merged. 
     */
    public boolean merge() {
        if (totesList.size() <= 1)
            return false;

        boolean isMerged = false;
        for (int windowStart = 0, windowEnd = 1; windowEnd < totesList.size(); windowStart++, windowEnd++) {
            while (!totesList.get(windowStart).isFull()) {
                Item item = removeFirstItem(windowEnd);
                addItem(item, windowStart);
                isMerged = true;
            }
        }
        return isMerged;
    }

    /**
     * Removes the first item in the tote from the specified list position from the
     * list of totes.
     * 
     * @param listPosition the position in the list of totes.
     * @return Item that was removed.
     */
    private Item removeFirstItem(int listPosition) {
        if (isToteCurrentlyFull(listPosition)) {
            numberOfFullTotes--;
            numberOfPartialTotes++;
        }
        Item item = totesList.get(listPosition).removeFirst();

        if (totesList.get(listPosition).getItemsList().size() == 0) {
            totesList.remove(listPosition);
            numberOfTotesCreated--;
            numberOfPartialTotes--;
        }

        itemQuantities--;
        return item;
    }

    /**
     * Removes an item from the totes list using a random position in the list.
     * 
     * @return int representing the tote id where the item was removed from.
     */
    public int removeItem() {
        Random randomNum = new Random();
        int listPosition = randomNum.nextInt(totesList.size());
        int toteId = totesList.get(listPosition).getToteId();
        removeFirstItem(listPosition);
        return toteId;
    }

    /**
     * Removes the last item in the tote from the specified list position.
     * 
     * @param listPosition the position in the list of totes.
     * @return Item that was removed.
     */
    private Item removeLastItem(int listPosition) {
        if (isToteCurrentlyFull(listPosition)) {
            numberOfFullTotes--;
            numberOfPartialTotes++;
        }
        Item item = totesList.get(listPosition).removeLast();

        if (totesList.get(listPosition).getItemsList().size() == 0) {
            totesList.remove(listPosition);
            numberOfTotesCreated--;
            numberOfPartialTotes--;
        }

        itemQuantities--;
        return item;
    }

    /**
     * Helper class for our TotesList
     */
    private class Tote {
        private List<Item> itemsList;
        private int toteID;

        /**
         * Constructs a new tote using the toteId being passed.
         * 
         * @param toteId the ID of this tote
         */
        private Tote(int toteId) {
            itemsList = new ArrayList<>();
            this.toteID = toteId;
        }
        /**
         * Adds an item to this tote.
         * 
         * @param item the item to be added.
         */
        private void addItem(Item item) {
            itemsList.add(item);
        }

        /**
         * Returns the {@link itemList} of this tote.
         * @return the {@link itemList}
         */
        private List<Item> getItemsList() {
            return itemsList;
        }

        /**
         * Returns the toteId for this tote.
         * @return the toteId for this tote.
         */
        private int getToteId() {
            return toteID;
        }

        /**
         * Returns true when this tote is full. 
         * @return true if this tote is full 
         */
        private boolean isFull() {
            return itemsList.size() == FileConstants.MAX_ITEMS_IN_TOTE;
        }

        /**
         * Removes the first item in {@link itemList}
         * 
         * @return the item that was removed from {@link itemList}
         */
        private Item removeFirst() {
            Item item = itemsList.get(0);
            itemsList.remove(0);
            return item;
        }

        /**
         * Removes the last item in {@link itemList}
         * @return the item that was removed from {@link itemList}
         */
        private Item removeLast() {
            int lastItem = itemsList.size() - 1;
            Item item = itemsList.get(lastItem);
            itemsList.remove(lastItem);
            return item;
        }
    }
}
