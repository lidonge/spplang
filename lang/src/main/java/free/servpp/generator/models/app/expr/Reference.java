package free.servpp.generator.models.app.expr;

import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.app.IQualifiedNameUtil;
import free.servpp.generator.general.app.SemanticException;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.app.SppFieldReference;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public class Reference extends Expression implements IPrimaryExpression, IQualifiedNameUtil {
    String value;

    @Override
    public String getExpr() {
        return NameUtil.getNameCall(value);
    }

    @Override
    public void check(SppDomain domain) throws SemanticException {
        SppFieldReference sppFieldReference = getQualifiedField(domain,null, value);
        if(sppFieldReference != null)
            setReturnType(sppFieldReference.getSppField().getParent());
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
