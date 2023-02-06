package main.java.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.java.constants.FileConstants;

public class MyError implements Runnable{

    String errorMessage;
    Class clasName;
    public MyError(Class className, String errorMessage) {
        this.clasName = className;
        this.errorMessage = errorMessage;
        
    }
    @Override
    public void run() {
        FileWriter writer;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = new Date();

            writer = new FileWriter(FileConstants.WRITE_FILE_EXCEPTIONS,true);
            writer.write("{\"ErrorIn\":" + "\"" + clasName.getSimpleName() + "\", " 
                    + "\"Message\":" + "\"" + errorMessage + "\", "
                    + "\"TimeStamp\":" + "\"" + dateFormat.format(date) + "\"}\n");
            writer.close();

        } catch (IOException e) {
            System.out.println("ERROR" + e.getMessage());
        }
    }    
}
