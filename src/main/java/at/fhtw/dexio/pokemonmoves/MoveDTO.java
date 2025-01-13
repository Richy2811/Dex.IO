package at.fhtw.dexio.pokemonmoves;

import at.fhtw.dexio.pokemontypes.TypeEntryDTO;

/**
 * Representation of the data structure which stores a variety of information
 * on the Pokémon move. This includes its name, power, type, etc. Only a subset
 * of this data is used here.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/pokemon">Moves</a> endpoint with the
 * id or name appended to the path.
 * <p>
 * Examples:
 * <ul>
 *     <li>Pound: https://pokeapi.co/api/v2/move/1</li>
 *     <li>Celebrate: https://pokeapi.co/api/v2/move/celebrate</li>
 *     <li>Genesis Supernova: https://pokeapi.co/api/v2/move/703</li>
 *     <li>Light that burns the sky: https://pokeapi.co/api/v2/move/light-that-burns-the-sky</li>
 * </ul>
 */
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
