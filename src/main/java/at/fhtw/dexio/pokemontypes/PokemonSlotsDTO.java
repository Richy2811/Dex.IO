package at.fhtw.dexio.pokemontypes;

import at.fhtw.dexio.pokedex.PokedexEntryDTO;

/**
 * Representation of the data structure which stores the Pokémon slot
 * (not included here) and {@link PokedexEntryDTO} of the Pokémon
 * who has this type.
 * @see TypeDTO
 */
public class PokemonSlotsDTO {
    private PokedexEntryDTO pokemon;

    public PokedexEntryDTO getPokemon() {
        return pokemon;
    }
}
