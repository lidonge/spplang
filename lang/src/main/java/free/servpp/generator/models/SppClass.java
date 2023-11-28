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
public class SppClass extends SppCompilationUnit {

    Map<String, SppField> sppFieldMap = new HashMap<>();
    List<SppField> sppFieldList = new ArrayList<>();

    private IScenarioHandler.CurrentCall currentCall;
    public SppClass(String name) {
        super(name);
    }

    public SppClass(String name, IConstance.CompilationUnitType type) {
        super(name);
        this.type = type;
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

    public Map<String, SppField> getSppFieldMap() {
        return sppFieldMap;
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
                return "Duplicate Field " + fieldName + " in class " + getName();
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
                "name='" + getName() + '\'' +
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

    public void copyFrom(SppCompilationUnit sppCompilationUnit) {
        super.copyFrom(sppCompilationUnit);
        SppClass cls = (SppClass) sppCompilationUnit;
        sppFieldMap = cls.sppFieldMap;
        sppFieldList = cls.sppFieldList;
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
