package free.servpp.generator.models.app.expr;

import free.servpp.generator.models.IComponent;
import free.servpp.generator.models.IContainer;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public interface IOperationExpression extends IExpression, IContainer {
    IExpression getLeft();
    IExpression getRight();
    void setLeft(IExpression left);
    void setRight(IExpression right);
    IOperation getOperation();

    @Override
    default void _add(IComponent component) {
        IOperationExpression current = this;
        IExpression expression = (IExpression) component;
        switch (current.getOperation().getType() ){
            case Left:
                current.setRight(expression);
                break;
            case Right:
                current.setLeft(expression);
                break;
            case Double:
                if(current.getLeft() == null){
                    current.setLeft(expression);
                }else{
                    current.setRight(expression);
                }
                break;
        }

    }

    @Override
    default String getExpr(){
        return getOperation().getExpr(getLeft(),getRight());
    }
}
