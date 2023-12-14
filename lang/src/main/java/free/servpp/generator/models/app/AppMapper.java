package free.servpp.generator.models.app;

import free.servpp.generator.models.Annotation;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppCompilationUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class AppMapper extends Annotation implements INamedObject{
    AppTable appTable;

    List<String> mapParameters = new ArrayList<>();
    List<MapperItem> mapperItemList = new ArrayList<>();

    public AppMapper(AppTable appTable) {
        this.appTable = appTable;
    }

    public String getName() {
        return appTable.getName();
    }

    public AppTable getAppTable() {
        return appTable;
    }

    public void setAppTable(AppTable appTable) {
        this.appTable = appTable;
    }

    public String addParameter(String parameter){
        if(mapParameters.indexOf(parameter) != -1)
            return "Duplicate name "+ parameter;
        mapParameters.add(parameter);
        return null;
    }
    public List<MapperItem> getMapperItemList() {
        return mapperItemList;
    }

    public String addMapperItem(MapperItem mapperItem){
        return mapperItem.addToList(mapperItemList,mapperItem);
    }

    @Override
    public String toString() {
        return "AppMapper{" +
                "sppClass=" + appTable.getName() +
                ", mapParameters=" + mapParameters +
                ", mapperItemList=" + mapperItemList +
                '}';
    }
}
