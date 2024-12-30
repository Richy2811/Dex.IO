package at.fhtw.dexio.networking;

import at.fhtw.dexio.pokemontypes.TypeDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.concurrent.Callable;

public class ConcurrentConnection implements Callable<TypeDTO> {
    //json mapper for deserialisation of TypeDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private final String url;

    public ConcurrentConnection(String url){
        this.url = url;
    }

    @Override
    public TypeDTO call() {
        String typeDTOJsonString = TcpConnectionHandler.getFromUrl(url);
        try {
            return jsonMapper.readValue(typeDTOJsonString, TypeDTO.class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
