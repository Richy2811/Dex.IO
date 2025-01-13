package at.fhtw.dexio.pokemonstats;

/**
 * Representation of the data structure which stores a variety of information
 * on the Pokémon stat, such as its affecting moves and natures, name, and id.
 * Only a subset of this data is used here.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/stat">Stats</a> endpoint with the
 * id or name appended to the path.
 * <p>
 * Examples:
 * <ul>
 *     <li>HP: https://pokeapi.co/api/v2/stat/1</li>
 *     <li>Attack: https://pokeapi.co/api/v2/stat/attack</li>
 *     <li>Defense: https://pokeapi.co/api/v2/stat/3</li>
 *     <li>Speed: https://pokeapi.co/api/v2/stat/speed</li>
 * </ul>
 */
public class StatDTO {
    private String name;

    public String getName() {
        return name;
    }
}
