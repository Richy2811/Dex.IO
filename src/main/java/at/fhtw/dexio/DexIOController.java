package at.fhtw.dexio;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokedex.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

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

    @FXML
    public void initialize() {
        //json mapper for deserialisation
        ObjectMapper jsonMapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();

        //get Pokédex entries from the PokeAPI and add it to the listview in the Pokédex tab
        String dexReturnStr = TcpConnectionHandler.getFromUrl("https://pokeapi.co/api/v2/pokemon");
        System.out.println(dexReturnStr);

        try{
            PokedexDTO pokedexDTO = jsonMapper.readValue(dexReturnStr, PokedexDTO.class);
            ObservableList<PokedexEntryDTO> dexentries = FXCollections.observableArrayList();
            for(PokedexEntryDTO entry: pokedexDTO.getResults()){
                entry.setName(entry.getName());
                dexentries.add(entry);
            }
            dexListView.setItems(dexentries);
        }
        catch (JsonProcessingException e){
            System.err.println(e.getMessage()); //TODO Handle exception in meaningful way
        }

        //add listener for selecting a Pokémon in the Pokédex tab
        dexListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //get information from attached url of PokedexDTO object
            try{
                String pokemonJsonStr = TcpConnectionHandler.getFromUrl(newValue.getURL());
                PokemonDTO pokemon = jsonMapper.readValue(pokemonJsonStr, PokemonDTO.class);

                //update Pokémon data in information section
                pokemonName.setText(pokemon.getName());

                type1.setText(pokemon.getTypes().getFirst().getType().getName());
                //show secondary type in case of dual typing
                if(pokemon.getTypes().size() == 2){
                    type2.setText(pokemon.getTypes().getLast().getType().getName());
                }

                //get Pokémon species information containing Pokédex entries
                String pokemonSpeciesJsonStr = TcpConnectionHandler.getFromUrl(pokemon.getSpecies().getUrl());
                PokemonSpeciesDTO pokemonSpecies = jsonMapper.readValue(pokemonSpeciesJsonStr, PokemonSpeciesDTO.class);
                //make sure the text is in english
                for(PokedexFlavorTextDTO entry : pokemonSpecies.getFlavor_text_entries()){
                    if(entry.getLanguage().getName().equals("en")){
                        pokedexFlavorText.setText(entry.getFlavor_text().replaceAll("\\u000c", " ").replaceAll("\\n", " "));
                    }
                }

                //show Pokémon sprite in information section
                Image spriteImage = new Image(pokemon.getSprites().getFront_default());
                pokemonImg.setImage(spriteImage);
            }
            catch (JsonProcessingException e){
                System.err.println(e.getMessage()); //TODO Handle exception in meaningful way
            }
        });
    }
}