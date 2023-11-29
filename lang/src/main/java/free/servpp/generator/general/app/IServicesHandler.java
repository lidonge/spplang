package free.servpp.generator.general.app;

import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.*;
import free.servpp.generator.models.app.AppService;
import free.servpp.generator.models.app.AppServices;
import free.servpp.generator.models.app.expr.*;
import free.servpp.lang.antlr.AppParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public interface IServicesHandler extends IApplicationHandler,IRecursionProcess {

    @Override
    default void enterService(AppParser.ServiceContext ctx) {
        String name = ctx.getChild(0).getText();
        AppService appService = new AppService();
        appService.setName(name);
        SppCompilationUnit sppClass = getSppDomian().getSppClass(NameUtil.firstToLowerCase(name, false));
        if (sppClass != null && sppClass instanceof SppService) {
            SppService sppService = (SppService) sppClass;
            appService.setSppService(sppService);
            sppService.setExpressions(appService.getAppExpressions());
            String err = getCurrentRuleBlock().getAppServices().addService(appService);
            setCurrentContainer(appService);
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
        setCurrentContainer(getTheAppService());
    }

    @Override
    default void enterAssignExpr(AppParser.AssignExprContext ctx) {
        String text = ctx.getChild(1).getText();
        AssignExpression assignExpression = getAssignExpression(text);
        addRecursionComponent(assignExpression);
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
        AppService appService = getTheAppService();
        SppLocalVar sppLocalVar = getQualifieField(text, appService.getSppService().getSppFieldMap());
        if (sppLocalVar == null)
            logSppError(ctx, text + " not defined");
        reference.setReturnType((SppClass) sppLocalVar.getType());
        addRecursionComponent(reference);
    }

    @Override
    default void exitPrimaryExprQualified(AppParser.PrimaryExprQualifiedContext ctx) {

    }

    @Override
    default void enterPrimaryExprLiteral(AppParser.PrimaryExprLiteralContext ctx) {
        String text = ctx.getChild(0).getText();
        Literal reference = new Literal();
        reference.setValue(text);
        addRecursionComponent(reference);

    }

    @Override
    default void exitPrimaryExprLiteral(AppParser.PrimaryExprLiteralContext ctx) {
    }

    @Override
    default void enterRightIncDecExpr(AppParser.RightIncDecExprContext ctx){
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Right, ()->{
            return new AssignOperation();
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitRightIncDecExpr(AppParser.RightIncDecExprContext ctx){
        exitCurrent();
    }

    @Override
    default void enterLeftIncDecAndSignExpr(AppParser.LeftIncDecAndSignExprContext ctx){
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Left,()->{
            return new ArithmeticOperation();
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitLeftIncDecAndSignExpr(AppParser.LeftIncDecAndSignExprContext ctx){
        exitCurrent();
    }

    @Override
    default void enterBitAndLogicNotExpr(AppParser.BitAndLogicNotExprContext ctx){
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Left, ()->{
            String text = ctx.getChild(0).getText();
            if(text.equals("~"))
                return new BitOperation();
            else
                return new LogicOperation();
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitBitAndLogicNotExpr(AppParser.BitAndLogicNotExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterDevMultModeExpr(AppParser.DevMultModeExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new ArithmeticOperation();
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitDevMultModeExpr(AppParser.DevMultModeExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterPlusMinusExpr(AppParser.PlusMinusExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new ArithmeticOperation();
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitPlusMinusExpr(AppParser.PlusMinusExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterShiftExpr(AppParser.ShiftExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addRecursionComponent(operationExpression);

    }

    @Override
    default void exitShiftExpr(AppParser.ShiftExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicGreatLessExpr(AppParser.LogicGreatLessExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addRecursionComponent(operationExpression);

    }

    @Override
    default void exitLogicGreatLessExpr(AppParser.LogicGreatLessExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicEqualsExpr(AppParser.LogicEqualsExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addRecursionComponent(operationExpression);

    }

    @Override
    default void exitLogicEqualsExpr(AppParser.LogicEqualsExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterBitAndExpr(AppParser.BitAndExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addRecursionComponent(operationExpression);

    }

    @Override
    default void exitBitAndExpr(AppParser.BitAndExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterBitXorExpr(AppParser.BitXorExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addRecursionComponent(operationExpression);

    }

    @Override
    default void exitBitXorExpr(AppParser.BitXorExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterBitOrExpr(AppParser.BitOrExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new BitOperation();
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitBitOrExpr(AppParser.BitOrExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicAndExpr(AppParser.LogicAndExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitLogicAndExpr(AppParser.LogicAndExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterLogicOrExpr(AppParser.LogicOrExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            return new LogicOperation();
        });
        addRecursionComponent(operationExpression);
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
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Left, ()->{
            BracketOperation bracketOperation = new BracketOperation();
            bracketOperation.setRightBracket(ctx.getChild(2).getText());
            return bracketOperation;
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitParenthesesExpr(AppParser.ParenthesesExprContext ctx) {
        exitCurrent();
    }

    @Override
    default void enterArrayExpr(AppParser.ArrayExprContext ctx) {
        OperationExpression operationExpression =createOperationExpression(ctx, IOperation.Opers_Type.Double, ()->{
            BracketOperation bracketOperation = new BracketOperation();
            bracketOperation.setRightBracket(ctx.getChild(3).getText());
            return bracketOperation;
        });
        addRecursionComponent(operationExpression);
    }

    @Override
    default void exitArrayExpr(AppParser.ArrayExprContext ctx) {
        exitCurrent();
    }

    interface IOperationCreator{
        IOperation createOperation();
    }
    private OperationExpression createOperationExpression(ParserRuleContext ctx,IOperation.Opers_Type type, IOperationCreator creator) {
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

    private AssignExpression getAssignExpression(String text) {
        AssignExpression assignExpression = new AssignExpression();
        String soper = text.substring(0, text.indexOf("="));
        AssignOperation assignOperation = new AssignOperation();
        assignOperation.setAssoc(soper);
        assignOperation.setType(IOperation.Opers_Type.Double);
        assignExpression.setOperation(assignOperation);
        return assignExpression;
    }

    private AppService getTheAppService() {
        AppServices appServices = getCurrentRuleBlock().getAppServices();
        List<AppService> appServiceList = appServices.getAppServiceList();
        AppService appService = getLastElement(appServiceList);
        return appService;
    }
}
