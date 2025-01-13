package at.fhtw.dexio.pokemonnatures;

/**
 * Representation of the data structure which stores the name and
 * URL of the Pokémon nature.
 * @see NatureDexDTO
 */
public class NatureEntryDTO {
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
