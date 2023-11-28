package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-28@version 1.0
 */
public class SppEnum extends SppCompilationUnit{
    List<String> items = new ArrayList<>();

    public SppEnum(String name) {
        super(name);
        setType(IConstance.CompilationUnitType.Enum);
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
