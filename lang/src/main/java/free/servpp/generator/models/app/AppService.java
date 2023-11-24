package free.servpp.generator.models.app;

import free.servpp.generator.models.SppService;
import free.servpp.generator.models.app.expr.IExpression;
import free.servpp.generator.models.app.expr.IOperationExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public class AppService  implements INamedObject{
    private String name;
    private SppService sppService;

    private List<IOperationExpression> expressions = new ArrayList<>();

    private IOperationExpression currentExpression;

    public IOperationExpression getCurrentExpression() {
        return currentExpression;
    }

    public void setCurrentExpression(IOperationExpression currentExpression) {
        this.currentExpression = currentExpression;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addExpression(IOperationExpression expression){
        expressions.add(expression);
    }

    public List<IOperationExpression> getAppExpressions() {
        return expressions;
    }

    public SppService getSppService() {
        return sppService;
    }

    public void setSppService(SppService sppService) {
        this.sppService = sppService;
    }
}
