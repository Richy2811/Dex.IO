package at.fhtw.dexio.pokemonmoves;

import java.util.List;

/**
 * Representation of the data structure which stores the name and
 * URL of every Pokémon move inside a list of {@link MoveEntryDTO} objects.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/move">Moves</a> endpoint.
 */
public class MoveDexDTO {
    private List<MoveEntryDTO> results;

    public List<MoveEntryDTO> getResults() {
        return results;
    }
}
