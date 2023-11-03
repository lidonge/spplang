package free.servpp.generator.checker;

import free.servpp.generator.IConstance;

import java.util.*;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class ClassChecker {
    Map<String, SppClass> mapsOfClass = new HashMap<>();
    List<ErrorContent> unFoundClass = new ArrayList<>();
    private SppClass currentClass = null;
    static final String[] primaryTypes = new String[]{
            "boolean", "char", "String"
            , "short", "int", "long"
            , "float", "double", "date"};

    public ClassChecker() {
        for(String primary: primaryTypes)
            addClass(new SppClass(primary));
    }

    public Map<String, SppClass> getMapsOfClass() {
        return mapsOfClass;
    }

    public SppClass getCurrentClass() {
        return currentClass;
    }

    public String checkClass(String clsName) {
        if (mapsOfClass.get(clsName) == null)
            return "Class " + clsName + " not found!";
        return null;
    }

    public String checkDupClass(String clsName) {
        if (mapsOfClass.get(clsName) != null)
            return "Class " + clsName + " duplicated!";
        return null;
    }

    public String addClass(SppClass cls) {
        String name = cls.getName();
        if (mapsOfClass.get(name) != null)
            return "Duplicate Class " + name;
        mapsOfClass.put(name, cls);
        currentClass = cls;
        return null;
    }

    public SppClass getSppClass(String clsName) {
        return mapsOfClass.get(clsName);
    }

    public void addUnFoundClass(ErrorContent errorContent){
        unFoundClass.add(errorContent);
    }

    public List<ErrorContent> checkAll(){
        ArrayList<ErrorContent> ret = new ArrayList<>();
        for(ErrorContent cont: unFoundClass){
            if(getSppClass(cont.getName()) == null){
                ret.add(cont);
            }
        }
        return ret;
    }
}
