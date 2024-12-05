package at.fhtw.dexio.pokedex;

import java.util.List;

public class PokedexDTO {
    private int count;
    private List<PokedexEntryDTO> results;

    public List<PokedexEntryDTO> getResults() {
        return results;
    }

    public Integer getCount(){
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setResults(List<PokedexEntryDTO> results) {
        this.results = results;
    }
}
