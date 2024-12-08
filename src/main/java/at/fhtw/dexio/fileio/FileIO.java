package at.fhtw.dexio.fileio;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for handling file export operations.
 */
public class FileIO {

    /**
     * Exports a single object to the user's desktop in JSON format.
     *
     * @param fileName the name of the file to be created
     * @param data the object to be exported
     */
    public static void exportToDesktop(String fileName, Object data) {
        try {
            // Get the user's desktop directory
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
            File file = new File(desktopPath, fileName);

            // Create an ObjectMapper instance for JSON serialization
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);

            // Write the serialized JSON data to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(jsonData);
                System.out.println("Data exported successfully to " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error during file export: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
