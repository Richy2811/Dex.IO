package at.fhtw.dexio.pokedex;

import java.util.List;

/**
 * Representation of the data structure which stores information about
 * the species of the Pokémon, such as its capture rate, its Pokédex entries,
 * its name, and its varieties. Only a subset of this data is used here.
 * Unlike the {@link PokemonDTO}, the Pokémon species may be shared between
 * multiple Pokémon. An example of this is <a href="https://pokeapi.co/api/v2/pokemon-species/shaymin">Shaymin</a>,
 * who has a <a href="https://pokeapi.co/api/v2/pokemon/shaymin-land">land</a> and a
 * <a href="https://pokeapi.co/api/v2/pokemon/shaymin-sky">sky</a> form. Both forms share
 * attributes in their species but may have different abilities, sprites, stats, types, etc.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/pokemon-species">Pokémon Species</a> endpoint with the
 * id or name appended to the path.
 * <p>
 * Examples:
 * <ul>
 *     <li>Bulbasaur: https://pokeapi.co/api/v2/pokemon-species/1</li>
 *     <li>Umbreon: https://pokeapi.co/api/v2/pokemon-species/umbreon</li>
 *     <li>Serperior: https://pokeapi.co/api/v2/pokemon-species/497</li>
 *     <li>Sylveon: https://pokeapi.co/api/v2/pokemon-species/sylveon</li>
 * </ul>
 */
public class PokemonSpeciesDTO {
    private List<PokedexFlavorTextDTO> flavor_text_entries;

    public List<PokedexFlavorTextDTO> getFlavor_text_entries() {
        return flavor_text_entries;
    }
}
