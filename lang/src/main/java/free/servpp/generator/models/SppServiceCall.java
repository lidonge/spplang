package free.servpp.generator.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppServiceCall implements IServiceBodyStatement{
    SppService callee;

    /**
     * For generate call parameter in mustache
     */
    Map<String, SppParameter> sppParameterHashMap = new HashMap<>();
    List<SppParameter> sppParameterList = new ArrayList<>();
    private ServiceBaseBody parent;

    public List<SppParameter> getSppParameterList() {
        return sppParameterList;
    }

    public String addParameter(SppParameter parameter){
        String name = parameter.getName();
        if( sppParameterHashMap.get(name) != null)
            return "Duplicate parameter " + name;
        sppParameterHashMap.put(name,parameter);
        sppParameterList.add(parameter);
        return null;
    }

    public SppParameter getParameter(String name){
        return sppParameterHashMap.get(name);
    }
    public SppService getCallee() {
        return callee;
    }

    public void setCallee(SppService callee) {
        this.callee = callee;
    }

    @Override
    public String toString() {
        return "SppServiceCall{" +
                "callee="+callee.getFuncName()+
                ", sppParameterList=" + sppParameterList +
                '}';
    }

    @Override
    public ServiceBaseBody getParent() {
        return parent;
    }

    @Override
    public void setParent(ServiceBaseBody parent) {
        this.parent = parent;
    }
}
