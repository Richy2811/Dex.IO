package at.fhtw.dexio.pokemontypes;

import java.util.List;

public class TypeDTO {
    private DamageRelationsDTO damage_relations;
    private Integer id;
    private String name;
    private List<PokemonSlotsDTO> pokemon;
    private GenerationTypeSpritesDTO sprites;

    public DamageRelationsDTO getDamage_relations() {
        return damage_relations;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public List<PokemonSlotsDTO> getPokemon() {
        return pokemon;
    }

    public GenerationTypeSpritesDTO getSprites() {
        return sprites;
    }

    @Override
    public String toString() {
        return getName();
    }
}
