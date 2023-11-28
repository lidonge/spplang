package free.servpp.generator.models;

/**
 * @author lidong@date 2023-11-28@version 1.0
 */
public interface IComponent {
    IContainer getParent();

    void setParent(IContainer iContainer);
}
