package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.ConcurrentNatureConnection;
import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokemonnatures.NatureDTO;
import at.fhtw.dexio.pokemonnatures.NatureDexDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * A service which, when started with a valid URL set, will return a {@link Map}
 * of {@link NatureDTO} when the task is finished, with the key value being the
 * name of the nature.
 */
public class NatureService extends Service<Map<String, NatureDTO>> {
    //json mapper for deserialisation of nature DTO objects
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private String natureDexURL;

    public void setNatureDexURL(String natureDexURL) {
        this.natureDexURL = natureDexURL;
    }

    public String getNatureDexURL() {
        return natureDexURL;
    }

    @Override
    protected Task<Map<String, NatureDTO>> createTask() {
        return new Task<>() {
            protected Map<String, NatureDTO> call() {
                String natureDexJsonString = TcpConnectionHandler.getFromUrl(natureDexURL);
                try {
                    NatureDexDTO natureDex = jsonMapper.readValue(natureDexJsonString, NatureDexDTO.class);

                    Map<String, NatureDTO> natureMap = new LinkedHashMap<>();

                    //start thread for each NatureDTO to reduce initial load times
                    List<FutureTask<NatureDTO>> natureFutureTaskPool = new ArrayList<>();
                    for(int i = 0; i < natureDex.getResults().size(); i++) {
                        //start new future task for each of the entries
                        String natureURL = natureDex.getResults().get(i).getUrl();
                        natureFutureTaskPool.add(new FutureTask<>(new ConcurrentNatureConnection(natureURL)));
                        Thread th = new Thread(natureFutureTaskPool.get(i));
                        th.start();
                    }

                    //get future tasks once finished and build map
                    for (FutureTask<NatureDTO> finishedTask : natureFutureTaskPool) {
                        //future tasks are blocked until they return which keeps the order
                        natureMap.put(finishedTask.get().getName(), finishedTask.get());
                    }

                    return natureMap;
                }
                catch (JsonProcessingException e){
                    return null;
                }
                catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
