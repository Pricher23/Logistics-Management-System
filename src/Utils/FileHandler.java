package Utils;

import java.io.*;
import java.util.*;

public class FileHandler {
    // Reads all lines from a file into a list
    public static List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {  // Skip empty lines
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't read the file: " + filename);
        }
        return lines;
    }

    // Writes a list of strings to a file
    public static void writeLines(String filename, List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Couldn't write to file: " + filename);
        }
    }

    // Splits a CSV line into its parts
    public static Map<String, String> parseCSVLine(String line) {
        Map<String, String> values = new HashMap<>();
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            values.put("id", parts[0].trim());
            values.put("name", parts[1].trim());
            values.put("priority", parts[2].trim());
            values.put("quantity", parts[3].trim());
        }
        return values;
    }
}
