package at.fhtw.dexio.pokemonmoves;

/**
 * Representation of the data structure which stores the name and
 * URL of the Pokémon move.
 * @see MoveDexDTO
 */
public class MoveEntryDTO {
    private String name;
    private String url;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName();
    }
}
