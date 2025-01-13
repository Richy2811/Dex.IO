package at.fhtw.dexio.pokedex;

import java.util.List;

/**
 * Representation of the data structure which stores the name and
 * URL of every Pokémon inside a list of {@link PokedexEntryDTO} objects.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/pokemon">Pokémon</a> endpoint.
 */
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
