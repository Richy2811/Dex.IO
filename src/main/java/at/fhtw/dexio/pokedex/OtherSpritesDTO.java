package at.fhtw.dexio.pokedex;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of the data structure which stores other sprite
 * sources of the Pokémon.
 * @see PokemonSpriteDTO
 */
public class OtherSpritesDTO {
    @JsonProperty("official-artwork")
    private OfficialArtworkSpritesDTO official_artwork;

    private ShowdownSpritesDTO showdown;

    public OfficialArtworkSpritesDTO getOfficial_artwork() {
        return official_artwork;
    }

    public ShowdownSpritesDTO getShowdown() {
        return showdown;
    }
}
