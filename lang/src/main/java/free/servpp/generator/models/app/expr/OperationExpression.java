package free.servpp.generator.models.app.expr;

import free.servpp.generator.general.app.SemanticException;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppDomain;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public class OperationExpression extends Expression implements IOperationExpression{

    IExpression left;
    IExpression right;
    IOperation operation;

    public String getExpr() {
        return  operation.getExpr(left, right);
    }

    @Override
    public SppClass getReturnType() {
        return returnType;
    }

    @Override
    public void check(SppDomain domain) throws SemanticException {
        operation.check(left,right);
    }

    public void setReturnType(SppClass returnType) {
        this.returnType = returnType;
    }

    @Override
    public IExpression getLeft() {
        return left;
    }

    public void setLeft(IExpression left) {
        this.left = left;
        if(left != null){
            left.setParent(this);
        }
    }

    @Override
    public IExpression getRight() {
        return right;
    }

    public void setRight(IExpression right) {
        this.right = right;
        right.setParent(this);
    }

    @Override
    public IOperation getOperation() {
        return operation;
    }

    public void setOperation(IOperation operation) {
        this.operation = operation;
    }
}
