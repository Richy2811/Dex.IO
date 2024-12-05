package at.fhtw.dexio.pokedex;

public class PokedexEntryDTO {
    private String name;
    private String url;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getURL() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        //list view uses toString() method to determine which text to display in cells
        return name;
    }
}
