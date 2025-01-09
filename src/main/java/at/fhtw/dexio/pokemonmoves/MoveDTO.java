package at.fhtw.dexio.pokemonmoves;

import at.fhtw.dexio.pokemontypes.TypeEntryDTO;

public class MoveDTO {
    private DamageClassEntryDTO damage_class;
    private String name;
    private Integer power;
    private TypeEntryDTO type;

    public DamageClassEntryDTO getDamage_class() {
        return damage_class;
    }

    public String getName() {
        return name;
    }

    public Integer getPower() {
        return power;
    }

    public TypeEntryDTO getType() {
        return type;
    }
}
