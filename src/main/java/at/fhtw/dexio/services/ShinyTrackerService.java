package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokedex.PokemonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ShinyTrackerService extends Service<PokemonDTO> {

    //json mapper for deserialisation of PokemonDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private String shinyPokemonInfoURL = null;

    public void setShinyPokemonInfoURL(String shinyPokemonInfoURL) {
        this.shinyPokemonInfoURL = shinyPokemonInfoURL;
    }

    public String getShinyPokemonInfoURL() {
        return shinyPokemonInfoURL;
    }

    @Override
    protected Task<PokemonDTO> createTask() {
        return new Task<>() {
            protected PokemonDTO call() {
                String shinyPokemonJsonString = TcpConnectionHandler.getFromUrl(shinyPokemonInfoURL);
                try {
                    return jsonMapper.readValue(shinyPokemonJsonString, PokemonDTO.class);
                }
                catch (JsonProcessingException e){
                    return null;
                }
            }
        };
    }
}
