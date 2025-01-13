package at.fhtw.dexio.pokemontypes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of the data structure which stores objects of each
 * generation of games containing information about the sprite used
 * for the Pokémon type.
 * @see TypeDTO
 */
public class GenerationTypeSpritesDTO {
    @JsonProperty("generation-ix")
    private GenerationIXTypeSpriteDTO generation_ix;

    public GenerationIXTypeSpriteDTO getGeneration_ix_type_sprites() {
        return generation_ix;
    }
}
