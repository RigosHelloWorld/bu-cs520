package main.java.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Order class represents an order that will be processed from the warehouse. 
 * @author Rigoberto Rosa
 */
public class Order {
    int orderNumber;
    Map<String,Integer> orderQuantitiesMap;

    public Order() {
        orderQuantitiesMap = new HashMap<>();
    }

    /**
     * Gets the orderNumber.
     * @return {@link Order#orderNumber}
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Gets the order quantities.
     * @return {@link Order#orderQuantitiesMap}
     */
    public Map<String,Integer> getOrderQuantities() {
        return orderQuantitiesMap;
    }

    /**
     * Sets the orderNumber.
     * @param orderNumber orderNumber of the Order
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Sets the orders.
     * @param upcCode upcCode for the item that is ordered. 
     */
    public void setOrderQuantitiesMap(String upcCode) {
        orderQuantitiesMap.put(upcCode, orderQuantitiesMap.getOrDefault(upcCode, 0) +1);
    }

    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Order " + getOrderNumber());
        for(Map.Entry<String,Integer> entry: orderQuantitiesMap.entrySet()) {
            int quantity = entry.getValue();
            while(quantity > 0) {
                stringBuilder.append(", " + entry.getKey());
                quantity--;
            }
        }
        return stringBuilder.toString();
    }
}
