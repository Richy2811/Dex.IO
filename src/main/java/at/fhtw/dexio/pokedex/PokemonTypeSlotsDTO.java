package at.fhtw.dexio.pokedex;

import at.fhtw.dexio.pokemontypes.TypeEntryDTO;

/**
 * Representation of the data structure which stores the type slot
 * (not included here) and {@link TypeEntryDTO} of the Pokémon.
 * @see PokemonDTO
 */
public class PokemonTypeSlotsDTO {
    private TypeEntryDTO type;

    public TypeEntryDTO getType() {
        return type;
    }
}
