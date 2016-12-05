package hw2;

import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.util.regex.*;

public class readWrite {

    public static void writeFile(String document, String fileName) {
        try (Writer writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMatrix(ArrayList<ArrayList<Double>> document, String fileName) {
        try (Writer writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
            for (ArrayList<Double> i: document) {
                for (Double j: i) {
                    String output = j.toString();
                    writer.write(output + " ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<String> readFileAsList(String fileName) {
        ArrayList<String> fileText = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                fileText.add(str);
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return fileText;
    }

    public static String readFileAsString(String fileName) {
        String fileText = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                fileText += str + "\n";
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return fileText;
    }

    public static List<String> splitByAuthor(String fileText) {
        String pattern = "==>.*<==";
        List<String> output = Arrays.asList(fileText.split(pattern));
        return output;
    }

}
