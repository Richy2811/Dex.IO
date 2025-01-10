package at.fhtw.dexio.services;

import at.fhtw.dexio.networking.ConcurrentTypeConnection;
import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokemontypes.TypeDTO;
import at.fhtw.dexio.pokemontypes.TypeDexDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TypeAPIService extends Service<Map<String, TypeDTO>> {
    //json mapper for deserialisation of TypeDexDTO object
    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private String typeURL;

    public String getTypeURL() {
        return typeURL;
    }

    public void setTypeURL(String typeURL) {
        this.typeURL = typeURL;
    }

    @Override
    protected Task<Map<String, TypeDTO>> createTask() {
        return new Task<>() {
            protected Map<String, TypeDTO> call() {
                String typeDexJsonString = TcpConnectionHandler.getFromUrl(typeURL);
                try{
                    //the TypeDexDTO contains a list of type entries which still need to be loaded from the API
                    TypeDexDTO typeDex = jsonMapper.readValue(typeDexJsonString, TypeDexDTO.class);

                    Map<String, TypeDTO> typeMap = new LinkedHashMap<>();
                    //add a default selection for choosing no type
                    typeMap.put("None", null);

                    //start a thread for each type entry to reduce initial load time
                    List<FutureTask<TypeDTO>> taskPool = new ArrayList<>();
                    for(int i = 0; i < typeDex.getResults().size(); i++){
                        String typeEntryURL = typeDex.getResults().get(i).getUrl();
                        //usage of FutureTask allows to wait for each task individually
                        taskPool.add(new FutureTask<>(new ConcurrentTypeConnection(typeEntryURL)));
                        Thread th = new Thread(taskPool.get(i));
                        th.start();
                    }

                    //add all types to map with type name as key
                    for(int i = 0; i < taskPool.size(); i++){
                        FutureTask<TypeDTO> tcpThread = taskPool.get(i);
                        //tcpThread.get() blocks until it returns a value, so TypeDTO objects are added in the right order
                        typeMap.put(typeDex.getResults().get(i).getName(), tcpThread.get());
                    }

                    return typeMap;
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
