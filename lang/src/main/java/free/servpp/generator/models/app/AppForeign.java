package free.servpp.generator.models.app;

import free.servpp.generator.models.SppCompilationUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-12-05@version 1.0
 */
public class AppForeign {
    List<SppFieldReference> keys = new ArrayList<>();
    private SppCompilationUnit foreignReference;

    public List<SppFieldReference> getKeys() {
        return keys;
    }

    public SppCompilationUnit getForeignReference() {
        return foreignReference;
    }

    public void setForeignReference(SppCompilationUnit foreignReference) {
        this.foreignReference = foreignReference;
    }
}
