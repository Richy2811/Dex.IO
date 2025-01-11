package at.fhtw.dexio.pokedex;

public class PokemonExportDTO {
    private String name;
    private String imageUrl;
    private String primaryType;
    private String secondaryType;
    private int hp; // Example: Include HP if needed

    public PokemonExportDTO(String name, String imageUrl, String primaryType, String secondaryType, int hp) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.hp = hp;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public String getSecondaryType() {
        return secondaryType;
    }

    public void setSecondaryType(String secondaryType) {
        this.secondaryType = secondaryType;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
