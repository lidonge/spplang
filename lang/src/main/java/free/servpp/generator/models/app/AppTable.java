package free.servpp.generator.models.app;

import free.servpp.generator.models.Annotation;
import free.servpp.generator.models.SppClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-12-05@version 1.0
 */
public class AppTable extends Annotation implements INamedObject{
    private String name;
    private SppClass entity;
    private List<AppColumn> appColumns = new ArrayList<>();
    private  List<SppFieldReference> primaryKeys = new ArrayList<>();
    private  List<AppForeign> foreignKeys = new ArrayList<>();

    public AppTable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SppClass getEntity() {
        return entity;
    }

    public void setEntity(SppClass entity) {
        this.entity = entity;
    }

    public List<AppColumn> getAppColumns() {
        return appColumns;
    }

    public boolean containsEntity(String name){
        return entity.getName().equals(name);
    }

    public List<SppFieldReference> getPrimaryKeys() {
        return primaryKeys;
    }

    public List<AppForeign> getForeignKeys() {
        return foreignKeys;
    }
}
