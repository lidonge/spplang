package free.servpp.generator.models;

import free.servpp.ILogable;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.ISppErrorLogger;
import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.app.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppDomain {
    Map<String, SppCompilationUnit> mapsOfClass = new HashMap<>();
    List<ErrorContent> unFoundClass = new ArrayList<>();
    List<Object[]> unknownServiceCall = new ArrayList<>();
    private SppCompilationUnit currentClass = null;

    private String name;
    private RuleBlock ruleBlock = new RuleBlock();

    public SppDomain(String name) {
        for(String primary: IConstance.primaryTypes)
            addClass(new SppClass(primary));
        this.name = name;
    }

    public List<Object[]> getUnknownServiceCall() {
        return unknownServiceCall;
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

    public void checkSemanticFinally(ISppErrorLogger listener){
        checkUnfoundClass(listener);
        checkUnknowServiceCall(listener);
    }

    private void checkUnknowServiceCall(ISppErrorLogger listener) {
        for(Object[] parameterChecker : unknownServiceCall){
            ParserRuleContext ctx = (ParserRuleContext) parameterChecker[0];
            SppClass currentCLass = (SppClass) parameterChecker[1];
            SppLocalVar localVar = (SppLocalVar) parameterChecker[2];
            SppService callee = (SppService) parameterChecker[3];
            int paramIndex = (int) parameterChecker[4];
            if(callee.getServiceBody().getSppLocalVarList().size() <= paramIndex){
                listener.logSppError(ctx,"Scenario " + currentCLass.getName() + ": Parameter " +
                        paramIndex + " not exist in service " + callee.getName());
            }else {
                SppLocalVar indexVar = callee.getServiceBody().getLocalVar(paramIndex);
                if (!isSameTypeWithParameter(indexVar, localVar))
                    listener.logSppError(ctx, "Parameter " + name + " is  excepted " + indexVar.getType().getName() + (indexVar.getArrayString()));
            }
        }
    }

    public boolean isSameTypeWithParameter(SppLocalVar indexVar, SppLocalVar nameVar) {
        boolean isSame = indexVar.getType().getName().equals(nameVar.getType().getName());
        isSame = isSame && indexVar.getArrayDimension() == nameVar.getArrayDimension();
        return isSame;
    }

    private void checkUnfoundClass(ISppErrorLogger listener) {
        ArrayList<ErrorContent> ret = new ArrayList<>();
        for(ErrorContent cont: unFoundClass){
            SppCompilationUnit sppClass = mapsOfClass.get(cont.getName());
            if(sppClass == null || !sppClass.isReal()){
                ret.add(cont);
            }
        }
        for(ErrorContent cont: ret){
            listener.logSppError(cont.getLine(), cont.getCol(), cont.getMsg());
        }
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

    public void dealEntityToRoleMaps() {
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

    public void generateDefaultServices(ILogable logger){
        Map<String, SppCompilationUnit> sppClassMap = getMapsOfClass();
        for (SppCompilationUnit sppClass : sppClassMap.values().toArray(new SppCompilationUnit[sppClassMap.size()])) {
            if (sppClass.getType() == IConstance.CompilationUnitType.entity) {
                SppService sppService = genDefaultService(logger, sppClass, sppClassMap,"Create", IConstance.ServiceType.update);

                sppService =genDefaultService(logger, sppClass, sppClassMap,"Get", IConstance.ServiceType.query);
                sppService.getReturns().addLocalVar(new SppLocalVar(sppClass,NameUtil.firstToLowerCase(sppClass.getName(),true)));

                sppService =genDefaultService(logger, sppClass, sppClassMap,"Search", IConstance.ServiceType.query);
                SppLocalVar sppLocalVar = new SppLocalVar(sppClass, NameUtil.firstToLowerCase(sppClass.getName(), true));
                sppLocalVar.setArrayDimension(1);
                sppService.getReturns().addLocalVar(sppLocalVar);
            }
        }
    }

    private SppService genDefaultService(ILogable logger, SppCompilationUnit sppClass, Map<String, SppCompilationUnit> sppClassMap,
                                          String prefix, IConstance.ServiceType serviceType) {
        String serviceName = prefix + sppClass.getName();
        SppCompilationUnit sppCompilationUnit = sppClassMap.get(serviceName);
        if(sppCompilationUnit == null) {
            sppCompilationUnit = new SppService(serviceName);
            addClass(sppCompilationUnit);
        }
        if(sppCompilationUnit instanceof SppService){
            sppCompilationUnit.setReal(true);
            SppService sppService = (SppService) sppCompilationUnit;
            sppService.setFuncName(NameUtil.firstToLowerCase(prefix,true) + sppClass.getName());
            sppService.setType(IConstance.CompilationUnitType.atomicservice);
            ScopeItem scopeItem = new ScopeItem();
            ScopeDefine scopeDefine = new ScopeDefine();
            scopeDefine.setLocal(true);
            scopeItem.setScopeDefine(scopeDefine);
            scopeItem.setService(sppService);
            sppService.setServiceType(serviceType);
            sppService.addLocalVar(new SppLocalVar(sppClass,NameUtil.firstToLowerCase(sppClass.getName(),true)));
            return sppService;
        }else{
            logger.getLogger().error("The unit {} conflict with default service of entity {}.", serviceName, sppClass.getName());
        }
        return null;
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
