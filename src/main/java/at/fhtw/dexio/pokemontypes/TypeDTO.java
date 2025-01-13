package at.fhtw.dexio.pokemontypes;

import java.util.List;

/**
 * Representation of the data structure which stores information on the Pokémon type.
 * This includes its damage relations, id, name, Pokémon who are of the same type,
 * sprites, etc. Only a subset of this data is used here.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/type">Types</a> endpoint with the
 * id or name appended to the path.
 * <p>
 * Examples:
 * <ul>
 *     <li>Normal: https://pokeapi.co/api/v2/type/1</li>
 *     <li>Water: https://pokeapi.co/api/v2/type/water</li>
 *     <li>Dragon: https://pokeapi.co/api/v2/type/16</li>
 *     <li>Fairy: https://pokeapi.co/api/v2/type/fairy</li>
 * </ul>
 */
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
