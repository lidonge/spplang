package free.servpp.generator.models.app.expr;

import free.servpp.generator.general.NameUtil;
import free.servpp.generator.general.app.SemanticException;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */

public class AssignOperation extends GeneralOperation implements IAssignOperation{
    String assoc;


    public AssignOperation() {
        setOperator("=");
    }

    @Override
    public String getAssocOperation() {
        return assoc;
    }

    public void setAssoc(String assoc) {
        this.assoc = assoc;
    }

    @Override
    public String getExpr(IExpression left, IExpression right) {
        String ret = "";
        if(left instanceof Reference){
            String setMethod = NameUtil.getNameCall(((Reference) left).getValue(),true);
            ret += setMethod +"(";
            if(assoc != null){
                ret += left.getExpr();
                ret += assoc;
            }
            ret += right == null ? "" : right.getExpr();

            ret += ")";
        } else {
            ret += left == null ? "" : left.getExpr();
            ret += assoc == null ? "" : assoc;
            ret += getOperator();
            ret += right == null ? "" : right.getExpr();
        }

        return ret;
    }

    @Override
    public void check(IExpression left, IExpression right) throws SemanticException {

    }
}
