package main.java;

import main.java.constants.FileConstants;
import main.java.warehouse.Warehouse;

public class Test {
    public static void main(String args[]) {
        Warehouse warehouse = new Warehouse();
        warehouse.LoadProducts(FileConstants.LOAD_PRODUCTS);
        warehouse.DisplayDetails();
        
        warehouse.FulfillOrders(FileConstants.LOAD_ORDERS);
        warehouse.DisplayDetails();

        warehouse.MergeTotes();
        warehouse.DisplayDetails();
    }
}
