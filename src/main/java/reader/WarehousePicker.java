package main.java.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import main.java.dao.Order;
import main.java.utils.UpcValidator;
import main.java.writer.MyError;

/**
 * WarehousePicker gets all the orders to be processed by reading from a file. 
 * @author Rigoberto Rosa
 */
public class WarehousePicker {
    File file;
    List<Order> orders;

    /**
     * Constructs a new WarehousePicker using a file. 
     * @param fileName to load the orders from
     * @throws FileNotFoundException
     */
    public WarehousePicker(String fileName) throws FileNotFoundException{
        file = new File(fileName);
        if(!file.exists()) throw new FileNotFoundException();
        orders = new ArrayList<>();
    }

    /**
     * Reads the file and returns a iterator of orders to be processed. 
     * @return an Iterator<Order> 
     */
    public Iterator<Order> getOrders() {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String order = "";
            while((order = reader.readLine()) != null) {
                processData(order);
            }
        } catch (IOException e ) {
            MyError error = new MyError(getClass(), e.getMessage());
            error.run();
        }
        return orders.iterator();
    }

    /**
     * The order to be added to the list.
     * @param order the order to be added to the orders list
     */
    private void loadOrder(Order order) {
        orders.add(order);
    }

    /**
     * Constructs a new order from a file. 
     * @param s the order to be processed
     */
    private void processData(String s) {
        StringTokenizer tokenz = new StringTokenizer(s,",");
        Order order = new Order();

        try {
            int orderNum = Integer.parseInt(tokenz.nextToken());
            order.setOrderNumber(orderNum);
            while(tokenz.hasMoreTokens()) {
                String token = tokenz.nextToken().trim();
                if (!UpcValidator.isValid(token)) 
                    throw new IllegalArgumentException("Upc Code " + token + " is invalid");
                order.setOrderQuantitiesMap(token);
            }
        } catch (NumberFormatException e) {
            MyError error = new MyError(getClass(), "Order number " + e.getMessage() + " is invalid");
            error.run();
        } catch (IllegalArgumentException e) {
            MyError error = new MyError(getClass(), e.getMessage());
            error.run();
        }
        loadOrder(order);
    }
}
