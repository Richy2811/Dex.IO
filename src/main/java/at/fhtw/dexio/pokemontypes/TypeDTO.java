package at.fhtw.dexio.pokemontypes;

public class TypeDTO {
    private DamageRelationsDTO damage_relations;
    private GenerationTypeSpritesDTO sprites;
    private String name;
    private Integer id;

    public DamageRelationsDTO getDamage_relations() {
        return damage_relations;
    }

    public GenerationTypeSpritesDTO getSprites() {
        return sprites;
    }

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
