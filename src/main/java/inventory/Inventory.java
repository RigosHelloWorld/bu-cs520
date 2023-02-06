package main.java.inventory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.constants.FileConstants;
import main.java.dao.Item;
import main.java.dao.Order;
import main.java.exceptions.WarehouseFullException;
import main.java.reader.WarehouseReceiver;
import main.java.utils.PrintSlow;
import main.java.writer.MyError;

/**
 * Inventory Class handles all the logic to add or remove an item from the
 * inventory.
 * 
 * @author Rigoberto Rosa
 */
public class Inventory {
    /**
     * Map that hold the UpcCode as a String and a referance to a TotesList.
     */
    private Map<String, TotesList> itemsMap;

    public Inventory() {
        itemsMap = new HashMap<>();
    }

    /**
     * Reads from a file and adds new items to our inventory.
     * 
     * @param fileName the file to load the {@link Item} inventory from
     * @throws FileNotFoundException
     */
    public void addNewInventory(String fileName) throws FileNotFoundException {
        WarehouseReceiver reciever = new WarehouseReceiver(fileName);
        Iterator<Item> items = reciever.unloadTruck();

        while (items.hasNext()) {
            Item item = items.next();
            updateItemsMap(item);
        }
    }

    /**
     * Returns a map with the <UpcCode, TotalTotes> (totalTotes = fullTotes +
     * partiallyFilledTotes)
     * 
     * @return map that holds the total amount of totes for the upcode.
     */
    public Map<String, Integer> getInventoryDetails() {
        Map<String, Integer> toteQuantitiesMap = new HashMap<>();
        for (Map.Entry<String, TotesList> entry : itemsMap.entrySet()) {
            toteQuantitiesMap.put(entry.getKey(), entry.getValue().getTotalTotes());
        }
        return toteQuantitiesMap;
    }

    /**
     * 
     * @return map that holds the upc code and quantities
     */
    public Map<String, Integer> getItemQuantities() {
        Map<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, TotesList> entry : itemsMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getItemQuantities());
        }
        return map;
    }

    /**
     * Returns the number of unused/unfilled totes.
     * 
     * @return int indicated number of unused totes in the warehouse
     */
    public int getNumOfEmptyTotes() {
        return FileConstants.TOTAL_TOTES_IN_WAREHOUSE - TotesList.numberOfTotesCreated;
    }

    /**
     * Returns the number of full totes.
     * 
     * @return int indicating the number of full totes in the warehouse
     */
    public int getNumOfFullTotes() {
        int fullTotes = 0;
        for (TotesList tote : itemsMap.values()) {
            fullTotes += tote.getNumOfFullTotes();
        }
        return fullTotes;
    }

    /**
     * Returns the number of partially filled totes.
     * 
     * @return int indicating the number of partially filled totes in the warehouse
     */
    public int getNumOfPartitalTote() {
        int partialTotes = 0;
        for (TotesList tote : itemsMap.values()) {
            partialTotes += tote.getNumOfPartialTotes();
        }
        return partialTotes;
    }

    /**
     * Checks to see if we can fill the entire order.
     * 
     * @param order to be filled
     * @return true when the entire order can be filled false otherwise
     */
    public boolean isOrderFullfillable(Order order) {
        for (Map.Entry<String, Integer> entry : order.getOrderQuantities().entrySet()) {
            if (itemsMap.get(entry.getKey()).getItemQuantities() - entry.getValue() < 0)
                return false;
        }
        return true;
    }

    /**
     * Merges partially filled totes.
     */
    public void mergeTotes() {

        for (Map.Entry<String, TotesList> entry : itemsMap.entrySet()) {
            if(entry.getValue().getNumOfPartialTotes() > 1) {
                int currentNumTotes = entry.getValue().getTotalTotes();
                entry.getValue().merge();
                System.out.println("Reduced " + entry.getKey() + " from " + currentNumTotes 
                    + " to " + entry.getValue().getTotalTotes());
            }
            else
                System.out.println("Reduction not needed for " + entry.getKey());
        }
    }

    /**
     * Prints to the console the upc code and which tote it is processed from.
     * 
     * @param toteId  the tote the item is being pulled from
     * @param upcCode the upc code for the item being processed
     */
    private void orderFullFilledFrom(int toteId, String upcCode) {
        System.out.println("Retrieving product from toteId: " + toteId
                + " for upc code: " + upcCode);
    }

    /**
     * Print to the console the Upccode and the item quantities for it.
     */
    public void printItemQuantities() {
        for (Map.Entry<String, TotesList> entry : itemsMap.entrySet()) {
            System.out.println("UPCCODE: " + entry.getKey() + " quantities: " + entry.getValue().getItemQuantities());
        }
    }

    /**
     * Processes an order and removes the item from the inventory
     * 
     * @param order to be processed by our inventory
     */
    public void processOrder(Order order) {
        System.out.println("Order fullfillment started: Order " + order.getOrderNumber() + "\n");
        for (Map.Entry<String, Integer> entry : order.getOrderQuantities().entrySet()) {
            int quantity = entry.getValue();
            while (quantity > 0) {
                orderFullFilledFrom(itemsMap.get(entry.getKey()).removeItem(), entry.getKey());
                ;
                quantity--;
            }
            System.out.println("Order fullfilled: " + order + "\n");
        }

    }

    /**
     * Adds an item to to the {@link itemsMap}
     * 
     * @param item to be added to the {@link itemsMap}
     */
    private void updateItemsMap(Item item) {
        // get the referance to the toteList or create a new totesList for the upccode
        TotesList totes = itemsMap.getOrDefault(item.getUpcCode(), new TotesList());
        try {
            if (totes.add(item)) {
                System.out.println("Used additional tote (" + TotesList.numberOfTotesCreated +
                        ") for UPC: " + item.getUpcCode());
            }
            itemsMap.put(item.getUpcCode(), totes);
        } catch (WarehouseFullException e) {
            MyError error = new MyError(getClass(), e.getMessage());
            error.run();
        }
    }
}
