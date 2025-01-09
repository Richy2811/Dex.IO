package at.fhtw.dexio.pokemonmoves;

public class DamageClassEntryDTO {
    private String name;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
