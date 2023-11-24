package free.servpp.generator.models.app.expr;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public interface IOperationExpression extends IExpression{
    IExpression getLeft();
    IExpression getRight();
    void setLeft(IExpression left);
    void setRight(IExpression right);
    IOperation getOperation();

    @Override
    default String getExpr(){
        return getOperation().getExpr(getLeft(),getRight());
    }
}
