package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokedex.PokemonSpeciesDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * A service which, when started with a valid URL set, will return the
 * {@link PokemonSpeciesDTO} of the requested Pokémon species when the
 * task is finished.
 */
public class PokemonSpeciesService extends Service<PokemonSpeciesDTO> {

    //json mapper for deserialisation of PokemonSpeciesDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private String pokemonSpeciesURL = null;

    public void setPokemonSpeciesURL(String pokemonURL) {
        this.pokemonSpeciesURL = pokemonURL;
    }

    public String getPokemonSpeciesURL() {
        return pokemonSpeciesURL;
    }

    @Override
    protected Task<PokemonSpeciesDTO> createTask() {
        return new Task<>() {
            protected PokemonSpeciesDTO call() {
                String pokemonSpeciesJsonString = TcpConnectionHandler.getFromUrl(pokemonSpeciesURL);
                try{
                    return jsonMapper.readValue(pokemonSpeciesJsonString, PokemonSpeciesDTO.class);
                }
                catch (JsonProcessingException e){
                    return null;
                }
            }
        };
    }
}
