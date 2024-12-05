package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokedex.PokemonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PokemonInfoService extends Service<PokemonDTO>{

    //json mapper for deserialisation of PokemonDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private String pokemonInfoURL = null;

    public void setPokemonInfoURL(String pokemonInfoURL) {
        this.pokemonInfoURL = pokemonInfoURL;
    }

    public String getPokemonInfoURL() {
        return pokemonInfoURL;
    }

    @Override
    protected Task<PokemonDTO> createTask() {
        return new Task<>() {
            protected PokemonDTO call() {
                String pokemonJsonString = TcpConnectionHandler.getFromUrl(pokemonInfoURL);
                try{
                    return jsonMapper.readValue(pokemonJsonString, PokemonDTO.class);
                }
                catch (JsonProcessingException e) {
                    return null;
                }
            }
        };
    }
}