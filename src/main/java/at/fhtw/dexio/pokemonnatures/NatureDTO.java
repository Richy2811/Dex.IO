package at.fhtw.dexio.pokemonnatures;

import at.fhtw.dexio.pokemonstats.StatEntryDTO;

/**
 * Representation of the data structure which stores a variety of information
 * on the Pokémon nature. This includes its increased stat, decreased stat, name, etc.
 * Only a subset of this data is used here.
 * <p>
 * This information is accessed through a request to the PokéAPI on the
 * <a href="https://pokeapi.co/api/v2/nature">Natures</a> endpoint with the
 * id or name appended to the path.
 * <p>
 * Examples:
 * <ul>
 *     <li>Hardy: https://pokeapi.co/api/v2/nature/1</li>
 *     <li>Modest: https://pokeapi.co/api/v2/nature/modest</li>
 *     <li>Timid: https://pokeapi.co/api/v2/nature/5</li>
 *     <li>Adamant: https://pokeapi.co/api/v2/nature/adamant</li>
 * </ul>
 */
public class NatureDTO {
    private StatEntryDTO decreased_stat;
    private StatEntryDTO increased_stat;
    private String name;

    public StatEntryDTO getDecreased_stat() {
        return decreased_stat;
    }

    public StatEntryDTO getIncreased_stat() {
        return increased_stat;
    }

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    public String toString() {
        return getName();
    }
}
