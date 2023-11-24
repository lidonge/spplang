package free.servpp.generator.models.app.expr;

import free.servpp.generator.models.SppClass;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public abstract class Expression implements IExpression{
    SppClass returnType;
    IOperationExpression parent;
    @Override
    public SppClass getReturnType() {
        return returnType;
    }

    public void setReturnType(SppClass returnType) {
        this.returnType = returnType;
    }

    @Override
    public IOperationExpression getParent() {
        return parent;
    }

    public void setParent(IOperationExpression parent) {
        this.parent = parent;
    }
}
