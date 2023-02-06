package main.java.utils;

/**
 * Class to validate a upcCode using the checksum digit. 
 *  @see <a href="https://sciencing.com/translate-bar-codes-7255594.html">Verify Upc code</a> 
 * @author Rigoberto Rosa
 */
public class UpcValidator {
    public static boolean isValid(String upcCode) {

        if(!validLength(upcCode)) return false;
        if(!validCheckSum(upcCode)) return false;
        return true;
    }
    private static boolean validCheckSum(String upcCode) {
        int sum = 0;
        for(int i = 0; i < upcCode.length()-1; i++) {
            sum += (i%2==0) ? (3*(upcCode.charAt(i)-48)) : upcCode.charAt(i) - 48;
        }
        int calcCheckDigit = (10 - (sum % 10)) % 10;
        return Character.getNumericValue(upcCode.charAt(upcCode.length()-1)) == calcCheckDigit;
    }

    private static boolean validLength(String upcCode) {
        final int upcCodeLength = 12;
        return upcCode.length() == upcCodeLength ? true : false;
    } 

    private UpcValidator(){}
}
