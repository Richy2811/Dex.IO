package at.fhtw.dexio;

import at.fhtw.dexio.pokedex.*;
import at.fhtw.dexio.services.PokedexService;
import at.fhtw.dexio.services.PokemonInfoService;
import at.fhtw.dexio.services.PokemonSpeciesService;
import at.fhtw.dexio.fileio.FileIO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Objects;


public class DexIOController {
    @FXML
    private ListView<PokedexEntryDTO> dexListView = new ListView<>();

    @FXML
    private ImageView pokemonImg = new ImageView();

    @FXML
    private Label pokemonName;

    @FXML
    private Label type1;

    @FXML
    private Label type2;

    @FXML
    private Text pokedexFlavorText;

    private final PokedexService pokedexEntryService = new PokedexService();
    private final PokemonInfoService pokemonInfoService = new PokemonInfoService();
    private final PokemonSpeciesService pokemonSpeciesService = new PokemonSpeciesService();

    private final Image placeHolderSprite = new Image(Objects.requireNonNull(DexIOController.class.getResource("images/Loading_Sprite.png")).toString(), true);





    @FXML
    public void initialize() {
        //get Pokédex entries from the PokeAPI
        pokedexEntryService.setPokedexURL("https://pokeapi.co/api/v2/pokemon/");
        pokedexEntryService.restart();

        //add listener for when the Pokédex object has been loaded
        pokedexEntryService.valueProperty().addListener((observable, oldEntries, newEntries) ->
                dexListView.setItems(newEntries));

        //add listener for selecting a Pokémon in the Pokédex tab
        dexListView.getSelectionModel().selectedItemProperty().addListener((observable, oldPokemonEntry, newPokemonEntry) -> {
            //stop every service which may potentially run at the time
            pokemonInfoService.cancel();
            pokemonSpeciesService.cancel();

            //clear any currently displayed information while the new one is loaded
            pokemonName.setText("...");
            type1.setText("...");
            type2.setText("");
            pokemonImg.setImage(placeHolderSprite);
            pokedexFlavorText.setText("...");


            //get information from attached url of PokedexDTO object
            pokemonInfoService.setPokemonInfoURL(newPokemonEntry.getURL());
            pokemonInfoService.restart();
        });

        //add listener for changes of the Pokémon info in the Pokédex tab (responsible for Pokémon name, types, and sprite)
        pokemonInfoService.valueProperty().addListener((observable, oldPokemonInfo, newPokemonInfo) -> {
            if(newPokemonInfo == null){
                return;
            }

            //collect Pokémon species information while editing UI
            pokemonSpeciesService.setPokemonSpeciesURL(newPokemonInfo.getSpecies().getUrl());
            pokemonSpeciesService.restart();

            //update Pokémon data in information section
            pokemonName.setText(newPokemonInfo.getName());

            type1.setText(newPokemonInfo.getTypes().getFirst().getType().getName());
            //show secondary type in case of dual typing
            if(newPokemonInfo.getTypes().size() == 2){
                type2.setText(newPokemonInfo.getTypes().getLast().getType().getName());
            }

            //show Pokémon sprite in information section
            Image spriteImage = new Image(newPokemonInfo.getSprites().getFront_default());
            pokemonImg.setImage(spriteImage);
        });

        //add listener for changes of the Pokémon species information in the Pokédex tab (responsible for Pokédex description)
        pokemonSpeciesService.valueProperty().addListener((observable, oldPokemonSpecies, newPokemonSpecies) -> {
            if(newPokemonSpecies == null){
                return;
            }

            //make sure the text is in english
            for(PokedexFlavorTextDTO entry : newPokemonSpecies.getFlavor_text_entries()){
                if(entry.getLanguage().getName().equals("en")){
                    pokedexFlavorText.setText(entry.getFlavor_text().replaceAll("\\u000c", " ").replaceAll("\\n", " "));
                }
            }
        });
    }
    @FXML
    private void handleExport() {
        // Get the selected Pokémon
        PokedexEntryDTO selectedPokemon = dexListView.getSelectionModel().getSelectedItem();

        if (selectedPokemon == null) {
            System.err.println("No Pokémon selected for export.");
            return;
        }

        // Use the Pokémon's name as the file name
        String fileName = selectedPokemon.getName() + ".json";

        // Export the selected Pokémon to the desktop
        FileIO.exportToDesktop(fileName, selectedPokemon);
    }
}



