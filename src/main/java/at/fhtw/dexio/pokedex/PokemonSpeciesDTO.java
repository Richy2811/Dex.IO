package at.fhtw.dexio.pokedex;

import java.util.List;

public class PokemonSpeciesDTO {
    private List<PokedexFlavorTextDTO> flavor_text_entries;

    public List<PokedexFlavorTextDTO> getFlavor_text_entries() {
        return flavor_text_entries;
    }
}
