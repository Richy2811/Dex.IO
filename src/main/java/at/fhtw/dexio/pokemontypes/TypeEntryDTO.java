package at.fhtw.dexio.pokemontypes;

public class TypeEntryDTO {
    private String name;
    private String url;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName();
    }
}
