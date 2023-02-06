package main.java.warehouse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import main.java.dao.Order;
import main.java.inventory.Inventory;
import main.java.inventory.TotesList;
import main.java.reader.WarehousePicker;
import main.java.utils.PrintSlow;

/**
 * @author Rigoberto Rosa
 */
public class Warehouse implements IWarehouse {
    Inventory inventory;
    List<Order> unfullfilledOrders;

    public Warehouse() {
        inventory = new Inventory();
        unfullfilledOrders = new ArrayList<>();
    }

    @Override
    public void DisplayDetails() {
        System.out.print("\nWAREHOUSE DETAILS ");
        PrintSlow.printSlow(" .....\n");
        getAllWarehouseTotes();
        getItemToToteQuantities();
        getItemQuantities();
    }

    @Override
    public void FulfillOrders(String filename) {
        Iterator<Order> orders;
        try {
            System.out.print("Fullfilling Orders ");
            PrintSlow.printSlow(" .....\n");
            WarehousePicker orderFiller = new WarehousePicker(filename);
            orders = orderFiller.getOrders();
            while (orders.hasNext()) {
                Order order = orders.next();
                if (!isOrderInStock(order)) {
                    unfullfilledOrders.add(order);
                    continue;
                }
                processData(order);
            }
        } catch (FileNotFoundException e) {
            System.out.println("\n\n\nLooks like someone called out sick (referance file) " + filename);
            e.printStackTrace();
        }

    }

    private void getAllWarehouseTotes() {
        int numOfFullTotes = inventory.getNumOfFullTotes();
        int numberOfPartialTotes = inventory.getNumOfPartitalTote();
        int numOfEmptyTotes = inventory.getNumOfEmptyTotes();
        System.out.println(numOfFullTotes + " Full Totes, "
                + numberOfPartialTotes + " Partial Totes, "
                + numOfEmptyTotes + " Empty Totes.\n");
    }

    private void getItemQuantities() {
        Map<String,Integer> map = inventory.getItemQuantities();
        System.out.printf("%-19s %s%n", "UPC", "In stock");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.printf("%-15s %12d%n", entry.getKey(), entry.getValue());
        }
        System.out.println();
    }

    private void getItemToToteQuantities() {
        System.out.printf("%-15s %s%n", "UPC", "Number Totes");
        for (Map.Entry<String, Integer> entry : inventory.getInventoryDetails().entrySet()) {
            System.out.printf("%-15s %12d%n", entry.getKey(), entry.getValue());
        }
        System.out.println();
    }

    private boolean isOrderInStock(Order order) {
        return inventory.isOrderFullfillable(order);
    }


    
    @Override
    public void LoadProducts(String filename) {
        System.out.print("LOADING PRODUCTS");
        PrintSlow.printSlow(" .....\n");
        try {
            inventory.addNewInventory(filename);
        } catch (FileNotFoundException e) {
            System.out.println("\n\n\nTruck got lost: (referance file) " + filename);
            // if we can't load items exit the program
            System.exit(0);
        }
    }

    @Override
    public boolean MergeTotes() {
        System.out.print("Merging partially filled totes ");
        PrintSlow.printSlow(" .....\n");
        inventory.mergeTotes();
        System.out.print("Merge complete ");
        PrintSlow.printSlow(" .....\n");
        return true;
    }

    private void processData(Order order) {
        inventory.processOrder(order);
    }
}
