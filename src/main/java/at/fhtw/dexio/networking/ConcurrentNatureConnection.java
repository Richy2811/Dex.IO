package at.fhtw.dexio.networking;

import at.fhtw.dexio.pokemonnatures.NatureDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.concurrent.Callable;

/**
 * Callable class which is used for multithreading with {@link java.util.concurrent.FutureTask}
 * inside the {@link at.fhtw.dexio.services.NatureService} class.
 * Returns the {@link NatureDTO} instance upon completion of task.
 */
public class ConcurrentNatureConnection implements Callable<NatureDTO> {
    //json mapper for deserialisation of NatureDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private final String natureURL;

    public ConcurrentNatureConnection(String url){
        this.natureURL = url;
    }

    @Override
    public NatureDTO call() throws Exception {
        String natureJsonString = TcpConnectionHandler.getFromUrl(natureURL);
        try {
            return jsonMapper.readValue(natureJsonString, NatureDTO.class);
        }
        catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
