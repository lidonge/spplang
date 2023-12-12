package free.servpp.generator.models.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppCompilationUnit;
import free.servpp.generator.util.NamedArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-12-05@version 1.0
 */
public class AppTable implements INamedObject{
    private String name;
    private NamedArray<SppClass> entities = new NamedArray<SppClass>();
    private List<AppColumn> appColumns = new ArrayList<>();
    private  List<SppFieldDefine> primaryKeys = new ArrayList<>();
    private  List<AppForeign> foreignKeys = new ArrayList<>();

    public AppTable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NamedArray<SppClass> getEntities() {
        return entities;
    }

    public List<AppColumn> getAppColumns() {
        return appColumns;
    }

    public boolean containsEntity(String name){
        return entities.containsKey(name);
    }

    public List<SppFieldDefine> getPrimaryKeys() {
        return primaryKeys;
    }

    public List<AppForeign> getForeignKeys() {
        return foreignKeys;
    }
}
