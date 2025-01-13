package at.fhtw.dexio.pokemontypes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of the data structure which stores objects of each
 * generation 9 Pokémon game, containing the sprite used for the
 * Pokémon type.
 * @see GenerationTypeSpritesDTO
 */
public class GenerationIXTypeSpriteDTO {
    @JsonProperty("scarlet-violet")
    private TypeSpriteDTO scarlet_violet;

    public TypeSpriteDTO getScarlet_violet_type_sprite() {
        return scarlet_violet;
    }
}
