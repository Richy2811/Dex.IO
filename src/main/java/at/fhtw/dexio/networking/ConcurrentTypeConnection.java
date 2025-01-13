package at.fhtw.dexio.networking;

import at.fhtw.dexio.pokemontypes.TypeDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.concurrent.Callable;

/**
 * Callable class which is used for multithreading with {@link java.util.concurrent.FutureTask}
 * inside the {@link at.fhtw.dexio.services.TypeAPIService} class.
 * Returns the {@link TypeDTO} instance upon completion of task.
 */
public class ConcurrentTypeConnection implements Callable<TypeDTO> {
    //json mapper for deserialisation of TypeDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private final String typeURL;

    public ConcurrentTypeConnection(String url){
        this.typeURL = url;
    }

    @Override
    public TypeDTO call() throws Exception {
        String typeDTOJsonString = TcpConnectionHandler.getFromUrl(typeURL);
        try {
            return jsonMapper.readValue(typeDTOJsonString, TypeDTO.class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
