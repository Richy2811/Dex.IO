package at.fhtw.dexio.pokedex;

import at.fhtw.dexio.pokemonmoves.PokemonMoveDTO;
import at.fhtw.dexio.pokemonstats.PokemonStatDTO;

import java.util.List;

/**
 * Representation of the data structure which stores a variety of information
 * on the Pokémon, inter alia its abilities, forms, locations, moves,
 * name, species, sprites, stats, and types. Only a subset of this data is used here.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/pokemon">Pokémon</a> endpoint with the
 * id or name appended to the path.
 * <p>
 * Examples:
 * <ul>
 *     <li>Bulbasaur: https://pokeapi.co/api/v2/pokemon/1</li>
 *     <li>Umbreon: https://pokeapi.co/api/v2/pokemon/umbreon</li>
 *     <li>Serperior: https://pokeapi.co/api/v2/pokemon/497</li>
 *     <li>Sylveon: https://pokeapi.co/api/v2/pokemon/sylveon</li>
 * </ul>
 */
public class PokemonDTO {
    private List<PokemonMoveDTO> moves;
    private String name;
    private PokemonSpeciesEntryDTO species;
    private PokemonSpriteDTO sprites;
    private List<PokemonStatDTO> stats;
    private List<PokemonTypeSlotsDTO> types;

    public List<PokemonMoveDTO> getMoves() {
        return moves;
    }

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public PokemonSpeciesEntryDTO getSpecies() {
        return species;
    }

    public PokemonSpriteDTO getSprites() {
        return sprites;
    }

    public List<PokemonStatDTO> getStats() {
        return stats;
    }

    public List<PokemonTypeSlotsDTO> getTypes() {
        return types;
    }
}
