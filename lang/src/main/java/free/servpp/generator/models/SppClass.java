package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.IScenarioHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppClass {
    private String basePackage;
    private String javaPackage;

    private String additionalPackage;
    String name;
    IConstance.CompilationUnitType type;
    Map<String, SppField> sppFieldMap = new HashMap<>();
    List<SppField> sppFieldList = new ArrayList<>();

    private boolean isReal = true;
    //There is only one method in a class, so not need
//    Map<String, SppMethod> methodMap = new HashMap<>();

    private IScenarioHandler.CurrentCall currentCall;
    public SppClass(String name) {
        this.name = name;
    }

    public SppClass(String name, IConstance.CompilationUnitType type) {
        this.name = name;
        this.type = type;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public String getAdditionalPackage() {
        return additionalPackage;
    }

    public void setAdditionalPackage(String additionalPackage) {
        this.additionalPackage = additionalPackage;
    }

    public String getPackageName() {
        if(additionalPackage != null)
            return basePackage +"." + javaPackage +"."+additionalPackage;
        else
            return basePackage +"." + javaPackage;
    }

    public boolean isReal() {
        return isReal;
    }

    public void setReal(boolean real) {
        isReal = real;
    }

    public List<SppField> getSppFieldList() {
        return sppFieldList;
    }

    public IScenarioHandler.CurrentCall getCurrentCall() {
        return currentCall;
    }

    public void setCurrentCall(IScenarioHandler.CurrentCall currentCall) {
        this.currentCall = currentCall;
    }

    public IConstance.CompilationUnitType getType() {
        return type;
    }

    public void setType(IConstance.CompilationUnitType type) {
        this.type = type;
    }

    public Map<String, SppField> getSppFieldMap() {
        return sppFieldMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String addField(SppField field){
//        if("QueryAccount".equals(getName()))
//            System.out.println(getName() +" add field "+ field.getName());
        String fieldName = field.getName();
        SppField theField = sppFieldMap.get(fieldName);
        if(  theField != null ) {
            if(type == IConstance.CompilationUnitType.scenario && theField.getType() == field.getType()){
                return null;
            }else
                return "Duplicate Field " + fieldName + " in class " + name;
        }
        field.setIndex(sppFieldMap.size());
        sppFieldMap.put(fieldName,field);
        sppFieldList.add(field);
        return null;
    }

    public SppField getField(String fieldName) {
        return sppFieldMap.get(fieldName);
    }
    public SppField getField(int index) {
        return sppFieldList.get(index);
    }

    @Override
    public String toString() {
        return "SppClass{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", sppFieldList=" + sppFieldList +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        boolean ret = super.equals(obj);

        if (obj instanceof SppClass){
            SppClass cls = (SppClass) obj;

            ret |= getName().equals(cls.getName());
        }
        return ret;
    }

    public void copyFrom(SppClass cls) {
        name = cls.name;
        type = cls.type;
        sppFieldMap = cls.sppFieldMap;
        sppFieldList = cls.sppFieldList;

        isReal = cls.isReal;

        currentCall = cls.currentCall;
    }

//    public String addMethod(SppMethod sppMethod){
//        String sppMethodName = sppMethod.getName();
//        if( methodMap.get(sppMethodName) != null)
//            return "Duplicate method " + sppMethodName + " in class " + name;
//        methodMap.put(sppMethodName,sppMethod);
//        return null;
//    }
//
//    public SppMethod getMethod(String methodName){
//        return methodMap.get(methodName);
//    }
}
