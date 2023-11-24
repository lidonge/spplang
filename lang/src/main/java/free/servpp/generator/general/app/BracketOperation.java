package free.servpp.generator.general.app;

import free.servpp.generator.models.app.expr.GeneralOperation;
import free.servpp.generator.models.app.expr.IExpression;
import free.servpp.generator.models.app.expr.IOperation;

/**
 * @author lidong@date 2023-11-24@version 1.0
 */
public class BracketOperation extends GeneralOperation implements IOperation {
    String rightBracket;
    @Override
    public String getExpr(IExpression left, IExpression right) {
        String ret = super.getExpr(left,right);
        ret += rightBracket;
        return ret;
    }

    public String getRightBracket() {
        return rightBracket;
    }

    public void setRightBracket(String rightBracket) {
        this.rightBracket = rightBracket;
    }

    @Override
    public void check(IExpression left, IExpression right) throws SemanticException {

    }
}
