package at.fhtw.dexio.pokedex;

/**
 * Representation of the data structure which stores the Pokédex
 * entry string of a specific Pokémon along with the {@link LanguageEntryDTO}
 * of the text's language.
 * @see PokemonSpeciesDTO
 */
public class PokedexFlavorTextDTO {
    private String flavor_text;
    private LanguageEntryDTO language;

    public String getFlavor_text() {
        return flavor_text;
    }

    public LanguageEntryDTO getLanguage() {
        return language;
    }
}
