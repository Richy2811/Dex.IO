package at.fhtw.dexio.fileio;

import at.fhtw.dexio.pokedex.PokemonExportDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class FileIO {

    /**
     * Exports a single object to the users desktop (could be changed) in JSON format.
     */
    public static void exportToDesktop(String fileName, Object data) {
        try {
            // Get the users desktop directory
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

    /**
     * Exports the entire Team as a html file with sprites and other info to the users Desktop
     */
    public static void exportTeamToHTML(String fileName, List<PokemonExportDTO> team) {
        try {
            // Get the users desktop directory
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
            File file = new File(desktopPath, fileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write the HTML structure
                writer.write("<!DOCTYPE html>\n<html>\n<head>\n");
                writer.write("<title>Pokemon Team</title>\n");
                writer.write("<style>\n");
                writer.write("body { font-family: Arial, sans-serif; }\n");
                writer.write(".pokemon { display: inline-block; text-align: center; margin: 20px; }\n");
                writer.write(".pokemon img { width: 150px; height: 150px; }\n");
                writer.write(".details { margin-top: 10px; }\n");
                writer.write("</style>\n</head>\n<body>\n");
                writer.write("<h1>Your Pokemon Team</h1>\n");

                // Add each Pokemon
                for (PokemonExportDTO pokemon : team) {
                    writer.write("<div class='pokemon'>\n");
                    writer.write("<img src='" + pokemon.getImageUrl() + "' alt='" + pokemon.getName() + "' />\n");
                    writer.write("<div class='details'>\n");
                    writer.write("<strong>" + pokemon.getName() + "</strong><br />\n");
                    writer.write("Type: " + pokemon.getPrimaryType());
                    if (pokemon.getSecondaryType() != null) {
                        writer.write(" / " + pokemon.getSecondaryType());
                    }
                    writer.write("<br />\n");
                    writer.write("</div>\n</div>\n");
                }

                writer.write("</body>\n</html>");
                System.out.println("Team exported successfully to HTML: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error during HTML export: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
