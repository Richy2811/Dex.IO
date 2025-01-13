package at.fhtw.dexio.pokemonnatures;

import java.util.List;

/**
 * Representation of the data structure which stores the name and
 * URL of every Pokémon nature inside a list of {@link NatureEntryDTO} objects.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/nature">Natures</a> endpoint.
 */
public class NatureDexDTO {
    List<NatureEntryDTO> results;

    public List<NatureEntryDTO> getResults() {
        return results;
    }
}
