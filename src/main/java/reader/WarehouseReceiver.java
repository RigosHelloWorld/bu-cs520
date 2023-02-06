package main.java.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.java.dao.Item;
import main.java.utils.UpcValidator;
import main.java.writer.MyError;

/**
 * WarehouseReciever recieves the inventory by reading from a file. 
 * @author Rigoberto Rosa
 */
public class WarehouseReceiver {
    File file;
    List<Item> items;

    /**
     * Constructs a new WarehouseReciever using the file. 
     * @param fileName the file to read from
     * @throws FileNotFoundException 
     */
    public WarehouseReceiver(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        if (!file.exists()) throw new FileNotFoundException();
        items = new ArrayList<>(); 
    }

    /**
     * The item to be added into the inventory.
     * @param item the item to be added into the inventory.
     */
    private void loadInventory(Item item) {
        items.add(item);
    }

    /**
     * Constructs a new item from the file. 
     * @param items to be added into the inventory
     * @throws IllegalArgumentException
     */
    private void processData(String items) throws IllegalArgumentException {
        String[] temp = items.split(",", 2);
        Item item = new Item(temp[0].trim(), temp[1].trim());
        if (!UpcValidator.isValid(item.getUpcCode()))
            throw new IllegalArgumentException("Upc Code " + item.getUpcCode() + " is invalid");
        loadInventory(item);
    }
    /**
     * Reads the file and returns the items to be added to the warehouse. 
     * @return an Iterator<Item>  
     */
    public Iterator<Item> unloadTruck() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String item = "";
            while ((item = reader.readLine()) != null) {
                try {
                    processData(item);
                } catch (IllegalArgumentException e) {
                    MyError error = new MyError(this.getClass(), e.getMessage());
                    error.run();
                    continue;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        return items.iterator();
    }
}
