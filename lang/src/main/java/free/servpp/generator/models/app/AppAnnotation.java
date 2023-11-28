package free.servpp.generator.models.app;

import free.servpp.generator.models.IComponent;
import free.servpp.generator.models.IContainer;
import free.servpp.generator.models.SppCompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-28@version 1.0
 */
public class AppAnnotation implements INamedObject, IContainer {
    private String name;

    private IContainer parent;
    private List<IComponent> children = new ArrayList<>();

    private List<SppCompilationUnit> units = new ArrayList<>();

    private Map<String,String> parameters = new HashMap<>();
    public AppAnnotation(String name) {
        this.name = name;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getQualifiedName(){
        String ret = "";
        if(parent != null && parent instanceof  AppAnnotation){
            ret = ((AppAnnotation) parent).getQualifiedName() +".";
        }
        return ret+ getName();
    }
    public List<SppCompilationUnit> getUnits() {
        return units;
    }

    public List<IComponent> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void _add(IComponent component) {
        children.add(component);
    }

    @Override
    public boolean isContains(IComponent component) {
        return children.indexOf(component) != -1;
    }

    @Override
    public IContainer getParent() {
        return parent;
    }

    @Override
    public void setParent(IContainer parent) {
        this.parent = parent;
    }

    public void addParameter(String name, String value) {
        parameters.put(name,value);
    }
}
