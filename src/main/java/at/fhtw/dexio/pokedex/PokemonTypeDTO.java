package at.fhtw.dexio.pokedex;

public class PokemonTypeDTO {
    private String name;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
