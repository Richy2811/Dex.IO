package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokemonmoves.MoveDexDTO;
import at.fhtw.dexio.pokemonmoves.MoveEntryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MovedexService extends Service<ObservableList<MoveEntryDTO>> {
    //json mapper for deserialisation of MoveDexDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private String moveDexURL;

    public void setMoveURL(String moveURL) {
        this.moveDexURL = moveURL;
    }

    public String getMoveURL() {
        return moveDexURL;
    }

    @Override
    protected Task<ObservableList<MoveEntryDTO>> createTask() {
        return new Task<>() {
            protected ObservableList<MoveEntryDTO> call() {
                String moveDexJsonString = TcpConnectionHandler.getFromUrl(moveDexURL);
                try{
                    MoveDexDTO moveDex = jsonMapper.readValue(moveDexJsonString, MoveDexDTO.class);

                    //return parsed move list
                    return FXCollections.observableList(moveDex.getResults());
                }
                catch(JsonProcessingException e){
                    return null;
                }
            }
        };
    }
}
