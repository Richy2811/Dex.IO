package at.fhtw.dexio.sorting;

import at.fhtw.dexio.pokedex.PokedexEntryDTO;
import at.fhtw.dexio.pokemontypes.PokemonSlotsDTO;
import at.fhtw.dexio.pokemontypes.TypeDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller class for the Sorting Menu.
 * Provides functionality to filter Pokémon by name and type, and update the displayed results dynamically.
 */
public class SortingController {

    @FXML
    private TextField sortForName; // TextField for filtering by name

    @FXML
    private ToggleGroup sortBy;

    @FXML
    private ToggleGroup order;

    @FXML
    private RadioButton sortByID;

    @FXML
    private RadioButton sortByName;

    @FXML
    private RadioButton sortAscending;

    @FXML
    private RadioButton sortDescending;

    @FXML
    private ComboBox<TypeDTO> sortForType; // ComboBox for filtering by type

    @FXML
    private Button applyButton; // Button to apply sorting

    private ListView<PokedexEntryDTO> dexListView;
    private List<PokedexEntryDTO> filterList;
    private Map<String, TypeDTO> pokemonTypeMap;

    /**
     * Sets the Pokémon entries to be used for sorting and filtering.
     */
    public void setSortingEntries(ObservableList<PokedexEntryDTO> entries, Map<String, TypeDTO> typeMap, ListView<PokedexEntryDTO> dexListView) {
        // Fill the observable list
        filterList = FXCollections.observableList(entries);

        // Fill the type ComboBox
        sortForType.setItems(FXCollections.observableArrayList(typeMap.values()));

        //create reference to type map
        pokemonTypeMap = typeMap;

        //create reference to Pokédex
        this.dexListView = dexListView;
    }

    /**
     * Applies filters to the Pokémon list based on the input in the name TextField and type ComboBox.
     * Sorts entries based on the selected options in ascending or descending order.
     * Updates the ListView with the filtered results.
     */
    private void applyFilters() {
        String nameFilter = sortForName.getText().toLowerCase();
        List<PokedexEntryDTO> filtered;
        //check if type filter was selected
        if(sortForType.getValue() != null) {
            String typeFilter = sortForType.getValue().getName();
            //gather all Pokémon of the chosen type and use this list to filter by name
            List<PokedexEntryDTO> typeFilterList = pokemonTypeMap.get(typeFilter).getPokemon().stream().map(PokemonSlotsDTO::getPokemon).toList();
            filtered = typeFilterList.stream().filter(entry -> entry.getName().toLowerCase().contains(nameFilter)).collect(Collectors.toList());
        }
        else{
            //filter by name
            filtered = filterList.stream().filter(entry -> entry.getName().toLowerCase().contains(nameFilter)).collect(Collectors.toList());
        }

        //apply sort
        if(sortByName.isSelected()){
            //use name to sort
            filtered.sort(Comparator.comparing(PokedexEntryDTO::getName));
        }
        if(sortDescending.isSelected()){
            //reverse list to get sorted entries in descending order
            Collections.reverse(filtered);
        }

        //update list view
        dexListView.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void applySorting() {
        applyFilters(); // Call the filter logic
        Stage stage = (Stage) applyButton.getScene().getWindow();
        stage.close(); // Close the current window
    }
}
