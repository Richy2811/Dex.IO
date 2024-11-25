package at.fhtw.dexio.pokedex;

import java.util.List;

public class PokemonDTO {
    private String name;
    private PokemonSpeciesUrlDTO species;
    private PokemonSpriteDTO sprites;
    private List<PokemonTypeSlotsDTO> types;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public PokemonSpeciesUrlDTO getSpecies() {
        return species;
    }

    public PokemonSpriteDTO getSprites() {
        return sprites;
    }

    public List<PokemonTypeSlotsDTO> getTypes() {
        return types;
    }
}
