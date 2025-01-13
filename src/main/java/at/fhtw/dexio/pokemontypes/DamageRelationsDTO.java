package at.fhtw.dexio.pokemontypes;

import java.util.List;

/**
 * Representation of the data structure which stores information about
 * type effectiveness in lists, containing objects of {@link TypeEntryDTO}.
 * @see TypeDTO
 */
public class DamageRelationsDTO {
    private List<TypeEntryDTO> double_damage_from;
    private List<TypeEntryDTO> double_damage_to;
    private List<TypeEntryDTO> half_damage_from;
    private List<TypeEntryDTO> half_damage_to;
    private List<TypeEntryDTO> no_damage_from;
    private List<TypeEntryDTO> no_damage_to;

    public List<TypeEntryDTO> getDouble_damage_from() {
        return double_damage_from;
    }

    public List<TypeEntryDTO> getDouble_damage_to() {
        return double_damage_to;
    }

    public List<TypeEntryDTO> getHalf_damage_from() {
        return half_damage_from;
    }

    public List<TypeEntryDTO> getHalf_damage_to() {
        return half_damage_to;
    }

    public List<TypeEntryDTO> getNo_damage_from() {
        return no_damage_from;
    }

    public List<TypeEntryDTO> getNo_damage_to() {
        return no_damage_to;
    }
}
