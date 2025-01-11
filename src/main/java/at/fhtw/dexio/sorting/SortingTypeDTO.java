package at.fhtw.dexio.sorting;

public class SortingTypeDTO {
    private String name;

    public SortingTypeDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name == null || name.isEmpty()
                ? name
                : name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    public String toString() {
        return getName();
    }
}
