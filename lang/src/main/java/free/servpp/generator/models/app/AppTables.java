package free.servpp.generator.models.app;

import free.servpp.generator.models.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-12-05@version 1.0
 */
public class AppTables extends Annotation implements INamedObject{
    String name;
    List<AppTable> appTableList = new ArrayList<>();

    public AppTables(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AppTable> getAppTableList() {
        return appTableList;
    }
}
