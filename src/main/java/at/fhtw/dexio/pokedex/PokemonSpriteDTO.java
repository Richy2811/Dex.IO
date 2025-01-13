package at.fhtw.dexio.pokedex;

/**
 * Representation of the data structure which stores sprite URLs
 * of the Pokémon, showing what it looks like from the front
 * and back, as well as its shiny form, its female sprites
 * (if the sprite has differences), and sprites from different games
 * and resources.
 * @see PokemonDTO
 */
public class PokemonSpriteDTO {
    private String front_default;
    private String front_shiny;

    public String getFront_default() {
        return front_default;
    }

    public String getFront_shiny() {
        return front_shiny;
    }
}
