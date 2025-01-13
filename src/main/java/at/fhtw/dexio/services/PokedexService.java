package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokedex.PokedexDTO;
import at.fhtw.dexio.pokedex.PokedexEntryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * A service which, when started with a valid URL set, will return an {@link ObservableList}
 * of {@link PokedexEntryDTO} when the task is finished.
 */
public class PokedexService extends Service<ObservableList<PokedexEntryDTO>> {
    //json mapper for deserialisation of PokedexDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private String pokedexURL = null;

    public void setPokedexURL(String pokedexURL) {
        this.pokedexURL = pokedexURL;
    }

    public String getPokedexURL() {
        return pokedexURL;
    }

    @Override
    protected Task<ObservableList<PokedexEntryDTO>> createTask() {
        return new Task<>() {
            protected ObservableList<PokedexEntryDTO> call() {
                String pokedexJsonString = TcpConnectionHandler.getFromUrl(pokedexURL);
                try {
                    PokedexDTO pokedex = jsonMapper.readValue(pokedexJsonString, PokedexDTO.class);

                    //build list which will be used to fill dexListView
                    ObservableList<PokedexEntryDTO> dexEntries = FXCollections.observableArrayList();
                    //add all entries to the listview in the Pokédex tab
                    for(PokedexEntryDTO entry: pokedex.getResults()){
                        entry.setName(entry.getName());
                        dexEntries.add(entry);
                    }
                    return dexEntries;
                }
                catch (JsonProcessingException e){
                    return null;
                }
            }
        };
    }
}
