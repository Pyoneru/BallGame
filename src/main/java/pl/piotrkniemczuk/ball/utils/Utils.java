package pl.piotrkniemczuk.ball.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Utils {

    public static Random random = new Random();

    public static long getTime(){
        return System.nanoTime();
    }


    /**
     * Read file as string
     * @param path path to file
     * @return read file in String
     */
    public static String readFileToString(String path){
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = "";
            while ((line = reader.readLine()) != null)
                builder.append(line + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
