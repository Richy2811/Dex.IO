package at.fhtw.dexio.pokemonmoves;

/**
 * Representation of the data structure which stores the name and
 * URL of the damage class the move has.
 * <p>
 * The different damage classes include:
 * <ul>
 *     <li>Status</li>
 *     <li>Physical</li>
 *     <li>Special</li>
 * </ul>
 * @see MoveDTO
 */
public class DamageClassEntryDTO {
    private String name;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
