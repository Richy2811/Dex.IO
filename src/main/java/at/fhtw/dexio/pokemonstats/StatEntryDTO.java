package at.fhtw.dexio.pokemonstats;

/**
 * Representation of the data structure which stores the name and
 * URL of the Pokémon stat.
 * @see StatDTO
 */
public class StatEntryDTO {
    private String name;
    private String url;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getUrl() {
        return url;
    }
}
