package free.servpp.generator.models.app.expr;

import free.servpp.generator.general.app.SemanticException;
import free.servpp.generator.models.IComponent;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppDomain;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public interface IExpression extends IComponent {

    SppClass getReturnType();
    String getExpr();
    void check(SppDomain domain) throws SemanticException;
}
