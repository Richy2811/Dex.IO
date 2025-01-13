package at.fhtw.dexio.pokedex;

import at.fhtw.dexio.pokemonmoves.PokemonMoveDTO;
import at.fhtw.dexio.pokemonstats.PokemonStatDTO;

import java.util.List;

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
