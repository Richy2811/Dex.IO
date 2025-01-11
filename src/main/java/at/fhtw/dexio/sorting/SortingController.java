package at.fhtw.dexio.sorting;

import at.fhtw.dexio.pokedex.PokedexEntryDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller class for the Sorting Menu.
 * Provides functionality to filter Pokemon by name and type, and update the displayed results dynamically.
 */
public class SortingController {

    @FXML
    private TextField sortForName; // TextField for filtering by name

    @FXML
    private ComboBox<String> sortForType; // ComboBox for filtering by type

    @FXML
    private ListView<SortingEntryDTO> sortingListView; // ListView to display Pokemon

    @FXML
    private Button applyButton; // Button to apply sorting

    @FXML
    private Button quitButton; // Button to quit the sorting menu

    private ObservableList<PokedexEntryDTO> pokedexEntries = FXCollections.observableArrayList();
    private ObservableList<SortingEntryDTO> sortingEntries = FXCollections.observableArrayList();

    /**
     * Initializes the SortingController.
     * Adds listeners to the filter fields (name and type) to dynamically update the displayed entries.
     */
    @FXML
    public void initialize() {
        // Add listeners for filtering
        sortForName.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        sortForType.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    /**
     * Sets the Pokemon entries to be used for sorting and filtering.
     */
    public void setSortingEntries(List<SortingEntryDTO> entries) {
        // Fill the observable list
        this.sortingEntries.setAll(entries);

        // Fill the type ComboBox
        Set<String> uniqueTypes = new HashSet<>();
        for (SortingEntryDTO entry : entries) {
            if (entry.getTypes() != null) {
                entry.getTypes().forEach(type -> uniqueTypes.add(type.getName()));
            }
        }
        sortForType.setItems(FXCollections.observableArrayList(uniqueTypes));

        // Display all entries initially in the ListView
        sortingListView.setItems(sortingEntries);
    }

    /**
     * Applies filters to the Pokemon list based on the input in the name TextField and type ComboBox.
     * Updates the ListView with the filtered results.
     */
    private void applyFilters() {
        String nameFilter = sortForName.getText().toLowerCase();
        String typeFilter = sortForType.getValue();

        // Apply filtering logic
        List<SortingEntryDTO> filteredList = sortingEntries.stream()
                .filter(entry -> entry.getName().toLowerCase().contains(nameFilter)) // Name filter
                .filter(entry -> {
                    if (typeFilter == null || typeFilter.isEmpty()) {
                        return true; // No type filter applied
                    }
                    return entry.getTypes() != null && entry.getTypes().stream()
                            .anyMatch(type -> type.getName().equalsIgnoreCase(typeFilter));
                }) // Type filter
                .toList();

        // Update the ListView with the filtered results
        sortingListView.setItems(FXCollections.observableArrayList(filteredList));
    }


    @FXML
    private void applySorting() {
        applyFilters(); // Call the filter logic
    }

    @FXML
    private void quitSorting() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close(); // Close the current window
    }

}
