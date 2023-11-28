package free.servpp.generator.models;

import free.servpp.generator.general.app.SemanticException;
import free.servpp.generator.models.app.IQualifiedNameUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class ServiceBody extends ServiceBaseBody implements IQualifiedNameUtil {
    SppClass realm;
    Map<String, SppLocalVar> sppLocalVarHashMap = new HashMap<>();
    List<SppLocalVar> sppLocalVarList = new ArrayList<>();

    public ServiceBody(SppClass realm) {
        this.realm = realm;
    }

    public List<SppLocalVar> getSppLocalVarList() {
        return sppLocalVarList;
    }

    public Map<String, SppLocalVar> getSppLocalVarHashMap() {
        return sppLocalVarHashMap;
    }

    public SppClass getRealm() {
        return realm;
    }

    public void setRealm(SppClass realm) {
        this.realm = realm;
    }

    public SppClass getQualifieField(String qualifiedName){
        return getQualifieField(qualifiedName,sppLocalVarHashMap);
    }

    public String checkQualifiedName(String qualifiedName){
        String[] parts = qualifiedName.split("\\.");
        SppLocalVar inst = sppLocalVarHashMap.get(parts[0]);
        if(inst == null)
            return "Var " + parts[0] + " not found!";
        SppClass cls = (SppClass) inst.getType();
        for(int i = 1;i<parts.length;i++){
            SppField field = cls.getField(parts[i]);
            if(field == null){
                return "Field " + parts[i] + " not found!";
            }
            cls = (SppClass) field.getType();
        }
        return null;
    }
    public String addLocalVar(SppLocalVar inst){
        String name = inst.getName();
        if( sppLocalVarHashMap.get(name) != null)
            return "Duplicate Instance " + name;
        sppLocalVarHashMap.put(name,inst);
        sppLocalVarList.add(inst);
        return null;
    }
    public SppLocalVar getLocalVar(String methodName){
        return sppLocalVarHashMap.get(methodName);
    }
    public SppLocalVar getLocalVar(int index){
        return sppLocalVarList.get(index);
    }

    @Override
    public String toString() {
        return "ServiceBody{" +
                "realm=" + realm.getName() +
                ", sppLocalVarList=" + sppLocalVarList +
                ", sppServiceCallList=" + sppServiceCallList +
                '}';
    }
}
