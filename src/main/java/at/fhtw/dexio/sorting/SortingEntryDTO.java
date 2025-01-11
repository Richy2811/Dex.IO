package at.fhtw.dexio.sorting;

import java.util.List;

public class SortingEntryDTO {
    private String name;
    private List<SortingTypeDTO> types;

    public SortingEntryDTO(String name, List<SortingTypeDTO> types) {
        this.name = name;
        this.types = types;
    }

    public String getName() {
        return name == null || name.isEmpty()
                ? name
                : name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public List<SortingTypeDTO> getTypes() {
        return types;
    }

    @Override
    public String toString() {
        return getName();
    }
}
