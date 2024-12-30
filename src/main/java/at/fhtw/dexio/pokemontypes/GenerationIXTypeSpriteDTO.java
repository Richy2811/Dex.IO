package at.fhtw.dexio.pokemontypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerationIXTypeSpriteDTO {
    @JsonProperty("scarlet-violet")
    private TypeSpriteDTO scarlet_violet;

    public TypeSpriteDTO getScarlet_violet_type_sprite() {
        return scarlet_violet;
    }
}
