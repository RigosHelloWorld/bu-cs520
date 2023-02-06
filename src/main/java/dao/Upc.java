package main.java.dao;

/**
 * UPC class represents a upcCode. 
 * @author Rigoberto Rosa
 */

public class Upc {
    public static boolean isValid(String upc) {
       
        int sum = 0;
        for(int i = 0; i < upc.length()-1; i++) {
            sum += (i%2==0) ? (3*(upc.charAt(i)-48)) : upc.charAt(i) - 48;
        }

        int calcCheckDigit = (10 - (sum % 10)) % 10;
        System.out.println(upc.charAt(upc.length()-1));
        System.out.println(calcCheckDigit);
        return Character.getNumericValue(upc.charAt(upc.length()-1)) == calcCheckDigit;

    }
    private String companyPrefix;
    private String itemReferance;
    private String checkSum;

    private String upcCode;

    public Upc(String upc) {
        this.upcCode = upc;
        setCodes(upc);
    }

    public String getUpcCode() {
        return upcCode;
    }

    private void setCodes(String upc) {
        this.companyPrefix = upc.substring(0, 6);
        this.itemReferance = upc.substring(6, 11);
        this.checkSum = upc.substring(11, upc.length());

    }
}
