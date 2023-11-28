package free.servpp.generator.models;

/**
 * @author lidong@date 2023-11-28@version 1.0
 */
public interface IContainer extends IComponent{
    default IComponent addComponent(IComponent component){
        if(isContains(component))
            return null;
        _add(component);
        component.setParent(this);
        return component;
    }

    void _add(IComponent component);

    boolean isContains(IComponent component);
}
