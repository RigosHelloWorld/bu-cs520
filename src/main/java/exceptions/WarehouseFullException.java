package main.java.exceptions;
import main.java.constants.FileConstants;
/**
 * Thrown when the warehouse if full and a new tote tries to be created. 
 * @see FileConstants#TOTAL_TOTES_IN_WAREHOUSE
 * @author Rigoberto Rosa
 */
public class WarehouseFullException extends Exception {
    public WarehouseFullException(String message) {
        super(message);
    }
}
