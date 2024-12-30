package at.fhtw.dexio.pokemontypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerationTypeSpritesDTO {
    @JsonProperty("generation-ix")
    private GenerationIXTypeSpriteDTO generation_ix;

    public GenerationIXTypeSpriteDTO getGeneration_ix_type_sprites() {
        return generation_ix;
    }
}
