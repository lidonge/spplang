package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;

import java.util.*;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class ClassChecker {
    Map<String, SppClass> mapsOfClass = new HashMap<>();
    List<ErrorContent> unFoundClass = new ArrayList<>();
    private SppClass currentClass = null;

    public ClassChecker() {
        for(String primary: IConstance.primaryTypes)
            addClass(new SppClass(primary));
    }

    public Map<String, SppClass> getMapsOfClass() {
        return mapsOfClass;
    }

    public SppClass getCurrentClass() {
        return currentClass;
    }

    public String checkClass(String clsName) {
        SppClass sppClass = mapsOfClass.get(clsName);
        if (sppClass == null || !sppClass.isReal())
            return "Class " + clsName + " not found!";
        return null;
    }

    public String checkDupClass(String clsName) {
        SppClass sppClass = mapsOfClass.get(clsName);
        if ( sppClass != null && sppClass.isReal())
            return "Class " + clsName + " duplicated!";
        return null;
    }

    public SppClass addClass(SppClass cls) {
        String name = cls.getName();
        SppClass sppClass = mapsOfClass.get(name);
        if ( sppClass != null) {
            sppClass.copyFrom(cls);
        }else {
            sppClass = cls;
            mapsOfClass.put(name, sppClass);
        }
        currentClass = sppClass;
        return sppClass;
    }

    public SppClass getSppClass(IConstance.CompilationUnitType type,String clsName) {
        SppClass sppClass = mapsOfClass.get(clsName);
        if(sppClass == null){
            sppClass = genObjectDecl(type,clsName);
            sppClass.setReal(false);
            mapsOfClass.put(clsName,sppClass);
        }
        return sppClass;
    }

    public void addUnFoundClass(ErrorContent errorContent){
        unFoundClass.add(errorContent);
    }

    public List<ErrorContent> checkAll(){
        ArrayList<ErrorContent> ret = new ArrayList<>();
        for(ErrorContent cont: unFoundClass){
            SppClass sppClass = mapsOfClass.get(cont.getName());
            if(sppClass == null || !sppClass.isReal()){
                ret.add(cont);
            }
        }
        return ret;
    }
    public static SppClass genObjectDecl(IConstance.CompilationUnitType type, String objName) {
        if(type == IConstance.CompilationUnitType.atomicservice | type == IConstance.CompilationUnitType.scenario)
            return new SppService(objName);
        if(type == IConstance.CompilationUnitType.rolemapper)
            return new SppRoleMapper(objName);
        return new SppClass(objName);
    }
}
