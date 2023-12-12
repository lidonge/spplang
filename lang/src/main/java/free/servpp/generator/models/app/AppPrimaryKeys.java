package free.servpp.generator.models.app;

import free.servpp.generator.models.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class AppPrimaryKeys extends Annotation {
    List<SppFieldDefine> keys = new ArrayList<>();

    public List<SppFieldDefine> getKeys() {
        return keys;
    }

    public String addKey(String qualifiedName, SppFieldDefine sppFieldDefine){
        if(keys.indexOf(sppFieldDefine) != -1){
            return "Duplicate primary key " + qualifiedName;
        }
        keys.add(sppFieldDefine);
        return null;
    }

    @Override
    public String toString() {
        return "PrimaryKeys{" +
                "keys=" + keys +
                '}';
    }
}
