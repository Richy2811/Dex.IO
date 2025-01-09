package at.fhtw.dexio.pokemonnatures;

import at.fhtw.dexio.pokemonstats.StatEntryDTO;

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
