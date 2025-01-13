package at.fhtw.dexio.pokemontypes;

import java.util.List;

/**
 * Representation of the data structure which stores the name and
 * URL of every Pokémon type inside a list of {@link TypeEntryDTO} objects.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/type">Types</a> endpoint.
 */
public class TypeDexDTO {
    private List<TypeEntryDTO> results;

    public List<TypeEntryDTO> getResults() {
        return results;
    }
}
