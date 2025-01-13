package at.fhtw.dexio.pokedex;

/**
 * Representation of the data structure which contains animated
 * sprites of the Pokémon from <a href="https://pokemonshowdown.com">Pokémon Showdown</a>,
 * stored as .gif file URLs.
 * @see OtherSpritesDTO
 */
public class ShowdownSpritesDTO {
    private String back_default;
    private String back_shiny;
    private String front_default;
    private String front_shiny;

    public String getBack_default() {
        return back_default;
    }

    public String getBack_shiny() {
        return back_shiny;
    }

    public String getFront_default() {
        return front_default;
    }

    public String getFront_shiny() {
        return front_shiny;
    }
}
