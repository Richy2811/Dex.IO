package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokemonmoves.MoveDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * A service which, when started with a valid URL set, will return the
 * {@link MoveDTO} of the requested move when the task is finished.
 */
public class MoveInfoService extends Service<MoveDTO> {
    //json mapper for deserialisation of MoveDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    String moveURL;

    public void setMoveURL(String moveURL) {
        this.moveURL = moveURL;
    }

    public String getMoveURL() {
        return moveURL;
    }

    @Override
    protected Task<MoveDTO> createTask() {
        return new Task<>() {
            protected MoveDTO call() {
                String moveJsonString = TcpConnectionHandler.getFromUrl(moveURL);
                try {
                    return jsonMapper.readValue(moveJsonString, MoveDTO.class);
                }
                catch (JsonProcessingException e){
                    return null;
                }
            }
        };
    }
}
