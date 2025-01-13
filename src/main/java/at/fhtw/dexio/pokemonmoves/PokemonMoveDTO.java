package at.fhtw.dexio.pokemonmoves;

/**
 * Representation of data structure which stores the move entry of
 * a move the Pokémon can learn or was able to learn at one point,
 * alongside version group details about the move (not included here).
 * @see at.fhtw.dexio.pokedex.PokemonDTO
 */
public class PokemonMoveDTO {
    private MoveEntryDTO move;

    public MoveEntryDTO getMove() {
        return move;
    }
}
