package main.java.utils;

/**
 * Class to print a string to the console slower.
 * @author Rigoberto Rosa
 */
public class PrintSlow {
    private static final int sleepTime = 350;
    public static void printSlow(String s) {
        try {
            for(char c : s.toCharArray()) {
                System.out.print(c);
                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR" + e.getMessage());
        }
    }    
    private PrintSlow(){}
}
