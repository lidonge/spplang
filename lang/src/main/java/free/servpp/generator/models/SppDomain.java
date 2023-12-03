package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.models.app.AnnotationDefine;
import free.servpp.generator.models.app.AppAnnotation;
import free.servpp.generator.models.app.RuleBlock;

import java.util.*;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppDomain {
    Map<String, SppCompilationUnit> mapsOfClass = new HashMap<>();
    List<ErrorContent> unFoundClass = new ArrayList<>();
    private SppCompilationUnit currentClass = null;

    private String name;
    private RuleBlock ruleBlock = new RuleBlock();

    public SppDomain(String name) {
        for(String primary: IConstance.primaryTypes)
            addClass(new SppClass(primary));
        this.name = name;
    }

    public RuleBlock getRuleBlock() {
        return ruleBlock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, SppCompilationUnit> getMapsOfClass() {
        return mapsOfClass;
    }

    public SppCompilationUnit getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(SppCompilationUnit currentClass) {
        this.currentClass = currentClass;
    }

    public String checkClass(String clsName) {
        SppCompilationUnit sppClass = mapsOfClass.get(clsName);
        if (sppClass == null || !sppClass.isReal())
            return "Class " + clsName + " not found!";
        return null;
    }

    public String checkDupClass(String clsName) {
        SppCompilationUnit sppClass = mapsOfClass.get(clsName);
        if ( sppClass != null && sppClass.isReal())
            return "Class " + clsName + " duplicated!";
        return null;
    }

    public SppCompilationUnit addClass(SppCompilationUnit cls) {
        String name = cls.getName();
        SppCompilationUnit sppClass = mapsOfClass.get(name);
        if ( sppClass != null) {
            sppClass.copyFrom(cls);
        }else {
            sppClass = cls;
            mapsOfClass.put(name, sppClass);
        }
        currentClass = sppClass;
        return sppClass;
    }

    public SppCompilationUnit getSppClass(String clsName) {
        return  mapsOfClass.get(clsName);
    }
    public SppCompilationUnit getSppClass(IConstance.CompilationUnitType type,String clsName) {
        SppCompilationUnit sppClass = mapsOfClass.get(clsName);
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
            SppCompilationUnit sppClass = mapsOfClass.get(cont.getName());
            if(sppClass == null || !sppClass.isReal()){
                ret.add(cont);
            }
        }
        return ret;
    }
    public static SppClass genObjectDecl(IConstance.CompilationUnitType type, String objName) {
        if(type == IConstance.CompilationUnitType.atomicservice)
            return new SppService(objName);
        if( type == IConstance.CompilationUnitType.scenario)
            return new SppScenario(objName);
        if(type == IConstance.CompilationUnitType.rolemapper)
            return new SppRoleMapper(objName);
        return new SppClass(objName);
    }

    public void dealMaps() {
        Map<String, SppCompilationUnit> sppClassMap = getMapsOfClass();
        for (SppCompilationUnit sppClass : sppClassMap.values()) {
            if (sppClass.getType() == IConstance.CompilationUnitType.rolemapper) {
//                System.out.println(sppClass);
                Map<String, String> entityToRole = new HashMap<>();
                SppRoleMapper sppRoleMapper = (SppRoleMapper) sppClass;
                for (SppLocalVar var : sppRoleMapper.getSppFieldList()) {
                    SppRoleField roleField = (SppRoleField) var;
                    entityToRole.put(roleField.getEntityName(), roleField.getName());
                }
                takeAll(sppRoleMapper, entityToRole);
            }
        }
    }

    public void dealAnnotations(List<? extends  IComponent> annotations){
        for(IComponent component:annotations){
            AppAnnotation annotation = (AppAnnotation) component;
            for(SppCompilationUnit unit : annotation.getUnits()){
                for(AnnotationDefine annotationDefine:annotation.getAnnotationDefineList()) {
                    unit.getAnnotations().add(annotationDefine);
                }
            }
            dealAnnotations(annotation.getChildren());
        }
    }

    private void takeAll(SppRoleMapper sppRoleMapper, Map<String, String> entityToRole) {
        if (sppRoleMapper.isTakeAll()) {
            SppClass entity = sppRoleMapper.getEntity();
            SppClass role = sppRoleMapper.getRole();
            for (SppLocalVar var : entity.getSppFieldList()) {
                if (entityToRole.get(var.getName()) == null)
                    role.addField((SppField) var);
            }
        }
    }
}
