package free.servpp.generator.general.app;

import free.servpp.generator.models.IComponent;
import free.servpp.generator.models.IContainer;

/**
 * @author lidong@date 2023-11-28@version 1.0
 */
public interface IRecursionProcess {
    IContainer getCurrentContainer();
    void setCurrentContainer(IContainer parent);

    default void exitCurrent() {
        IContainer current = getCurrentContainer();
        setCurrentContainer((IContainer) current.getParent());
    }

    default IComponent addRecursionComponent(IComponent component) {
        IContainer current = getCurrentContainer();
        current.addComponent(component);
        if(component instanceof IContainer)
            setCurrentContainer((IContainer) component);
        return component;
    }
}
