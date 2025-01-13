package at.fhtw.dexio.pokedex;

/**
 * Representation of the data structure which contains
 * URLs of the official artwork of the Pokémon.
 * @see OtherSpritesDTO
 */
public class OfficialArtworkSpritesDTO {
    private String front_default;
    private String front_shiny;

    public String getFront_default() {
        return front_default;
    }

    public String getFront_shiny() {
        return front_shiny;
    }
}
