package free.servpp.generator.general.app;

import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppService;
import free.servpp.generator.models.app.AppService;
import free.servpp.generator.models.app.AppServices;
import free.servpp.generator.models.app.expr.*;
import free.servpp.lang.antlr.AppParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public interface IServicesHandler extends IApplicationHandler {

    @Override
    default void enterService(AppParser.ServiceContext ctx) {
        String name = ctx.getChild(0).getText();
        AppService appService = new AppService();
        appService.setName(name);
        SppClass sppClass = getSppDomian().getSppClass(NameUtil.firstToLowerCase(name, false));
        if (sppClass != null && sppClass instanceof SppService) {
            SppService sppService = (SppService) sppClass;
            appService.setSppService(sppService);
            sppService.setExpressions(appService.getAppExpressions());
            String err = getCurrentRuleBlock().getAppServices().addService(appService);
            if (err != null)
                logSppError(ctx, err);
        } else {
            logSppError(ctx, name + " is null or not a service.");
        }
    }

    @Override
    default void enterExpressionStatement(AppParser.ExpressionStatementContext ctx) {
    }

    @Override
    default void exitExpressionStatement(AppParser.ExpressionStatementContext ctx) {
        AppService appService = getAppService();
        appService.setCurrentExpression(null);
    }

    @Override
    default void enterAssignExpr(AppParser.AssignExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        String text = ctx.getChild(1).getText();
        AssignExpression assignExpression = getAssignExpression(text);
        addExpression(current, appService, assignExpression);
    }
    @Override
    default void exitAssignExpr(AppParser.AssignExprContext ctx){
        exitCurrent();
    }

    @Override
    default void enterPrimaryExprQualified(AppParser.PrimaryExprQualifiedContext ctx) {
        String text = ctx.getChild(0).getText();
        Reference reference = new Reference();
        reference.setValue(text);
        AppService appService = getAppService();
        SppClass sppClass = getQualifieField(text, appService.getSppService().getSppFieldMap());
        if (sppClass == null)
            logSppError(ctx, text + " not defined");
        reference.setReturnType(sppClass);
        addToCurrent(appService.getCurrentExpression(),reference);
    }

    @Override
    default void exitPrimaryExprQualified(AppParser.PrimaryExprQualifiedContext ctx) {

    }

    @Override
    default void enterPrimaryExprLiteral(AppParser.PrimaryExprLiteralContext ctx) {
        String text = ctx.getChild(0).getText();
        Literal reference = new Literal();
        reference.setValue(text);
        AppService appService = getAppService();
//        reference.setReturnType(sppClass);
        addToCurrent(appService.getCurrentExpression(),reference);

    }

    @Override
    default void exitPrimaryExprLiteral(AppParser.PrimaryExprLiteralContext ctx) {
    }

    @Override
    default void enterRightIncDecExpr(AppParser.RightIncDecExprContext ctx){
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Right, ()->{
            return new AssignOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitRightIncDecExpr(AppParser.RightIncDecExprContext ctx){
        exitCurrent();
    }

    @Override
    default void enterLeftIncDecAndSignExpr(AppParser.LeftIncDecAndSignExprContext ctx){
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Left,()->{
            return new ArithmeticOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitLeftIncDecAndSignExpr(AppParser.LeftIncDecAndSignExprContext ctx){
        exitCurrent();
    }

    @Override
    default void enterBitAndLogicNotExpr(AppParser.BitAndLogicNotExprContext ctx){
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Left, ()->{
            String text = ctx.getChild(0).getText();
            if(text.equals("~"))
                return new BitOperation();
            else
                return new LogicOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitBitAndLogicNotExpr(AppParser.BitAndLogicNotExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterDevMultModeExpr(AppParser.DevMultModeExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new ArithmeticOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitDevMultModeExpr(AppParser.DevMultModeExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterPlusMinusExpr(AppParser.PlusMinusExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new ArithmeticOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitPlusMinusExpr(AppParser.PlusMinusExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterShiftExpr(AppParser.ShiftExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addExpression(current,appService,operationExpression);

    }

    @Override
    default void exitShiftExpr(AppParser.ShiftExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicGreatLessExpr(AppParser.LogicGreatLessExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addExpression(current,appService,operationExpression);

    }

    @Override
    default void exitLogicGreatLessExpr(AppParser.LogicGreatLessExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicEqualsExpr(AppParser.LogicEqualsExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addExpression(current,appService,operationExpression);

    }

    @Override
    default void exitLogicEqualsExpr(AppParser.LogicEqualsExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterBitAndExpr(AppParser.BitAndExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addExpression(current,appService,operationExpression);

    }

    @Override
    default void exitBitAndExpr(AppParser.BitAndExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterBitXorExpr(AppParser.BitXorExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addExpression(current,appService,operationExpression);

    }

    @Override
    default void exitBitXorExpr(AppParser.BitXorExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterBitOrExpr(AppParser.BitOrExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitBitOrExpr(AppParser.BitOrExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicAndExpr(AppParser.LogicAndExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitLogicAndExpr(AppParser.LogicAndExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicOrExpr(AppParser.LogicOrExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitLogicOrExpr(AppParser.LogicOrExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterQuestionExpr(AppParser.QuestionExprContext ctx) {

    }

    @Override
    default void exitQuestionExpr(AppParser.QuestionExprContext ctx) {

    }

    @Override
    default void enterParenthesesExpr(AppParser.ParenthesesExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Left, ()->{
            BracketOperation bracketOperation = new BracketOperation();
            bracketOperation.setRightBracket(ctx.getChild(2).getText());
            return bracketOperation;
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitParenthesesExpr(AppParser.ParenthesesExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterArrayExpr(AppParser.ArrayExprContext ctx) {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        OperationExpression operationExpression =createOperationExpression(ctx, appService, IOperation.Opers_Type.Double, ()->{
            BracketOperation bracketOperation = new BracketOperation();
            bracketOperation.setRightBracket(ctx.getChild(3).getText());
            return bracketOperation;
        });
        addExpression(current,appService,operationExpression);
    }

    @Override
    default void exitArrayExpr(AppParser.ArrayExprContext ctx) {
        exitCurrent();
    }

    interface IOperationCreator{
        IOperation createOperation();
    }
    private OperationExpression createOperationExpression(ParserRuleContext ctx, AppService appService, IOperation.Opers_Type type, IOperationCreator creator) {
        OperationExpression operationExpression = new OperationExpression();
        int pos = 0;
        switch (type){
            case Right:
            case Double:
                pos = 1;
                break;
        }
        String text = ctx.getChild(pos).getText();
        IOperation operation = creator.createOperation();

        operation.setOperator(text);
        operation.setType(type);
        operationExpression.setOperation(operation);
        return operationExpression;
    }

    private AppService getAppService() {
        AppServices appServices = getCurrentRuleBlock().getAppServices();
        List<AppService> appServiceList = appServices.getAppServiceList();
        AppService appService = getLastElement(appServiceList);
        return appService;
    }

    private static void addToCurrent(IOperationExpression current, IExpression assignExpression) {
        switch (current.getOperation().getType() ){
            case Left:
                current.setRight(assignExpression);
                break;
            case Right:
                current.setLeft(assignExpression);
                break;
            case Double:
                if(current.getLeft() == null){
                    current.setLeft(assignExpression);
                }else{
                    current.setRight(assignExpression);
                }
                break;
        }
    }

    private AssignExpression getAssignExpression(String text) {
        AssignExpression assignExpression = new AssignExpression();
        String soper = text.substring(0, text.indexOf("="));
        AssignOperation assignOperation = new AssignOperation();
        assignOperation.setAssoc(soper);
        assignOperation.setType(IOperation.Opers_Type.Double);
        assignExpression.setOperation(assignOperation);
        return assignExpression;
    }

    private void exitCurrent() {
        AppService appService = getAppService();
        IOperationExpression current = appService.getCurrentExpression();
        appService.setCurrentExpression(current.getParent());
    }

    private void addExpression(IOperationExpression current, AppService appService, IExpression expression) {
        if(current == null)
            appService.addExpression((IOperationExpression) expression);
        else {
            addToCurrent(current, expression);
        }
        if(expression instanceof IOperationExpression)
            appService.setCurrentExpression((IOperationExpression) expression);
    }
}
