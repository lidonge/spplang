package free.servpp.generator.models.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class AppMapper implements INamedObject{
    SppClass sppClass;

    List<String> mapParameters = new ArrayList<>();
    List<MapperItem> mapperItemList = new ArrayList<>();

    public AppMapper(SppClass sppClass) {
        this.sppClass = sppClass;
    }

    public String getName() {
        return sppClass.getName();
    }

    public SppClass getSppClass() {
        return sppClass;
    }

    public void setSppClass(SppClass sppClass) {
        this.sppClass = sppClass;
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
                "sppClass=" + sppClass.getName() +
                ", mapParameters=" + mapParameters +
                ", mapperItemList=" + mapperItemList +
                '}';
    }
}
