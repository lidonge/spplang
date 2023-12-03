package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IScenarioHandler extends IServiceDefinitionHandler {
    @Override
    default void enterScenario(SppParser.ScenarioContext ctx) {
//        push(CompilationUnitType.scenario);
//        push(ScenarioType.parallel);
        SppScenario currentClass = new SppScenario("");
        currentClass.setType(CompilationUnitType.scenario);
        currentClass.setScenarioType(ScenarioType.parallel);
        getSppDomain().setCurrentClass(currentClass);
    }

    @Override
    default void exitScenario(SppParser.ScenarioContext ctx) {
        getSppDomain().setCurrentClass(null);
    }
    @Override
    default void enterAutosort(SppParser.AutosortContext ctx) {
        String st = ctx.getChild(0).getText();
        IConstance.ScenarioType scenarioType = ScenarioType.valueOf(st);
        SppScenario sppScenario = (SppScenario) getSppDomain().getCurrentClass();
        sppScenario.setScenarioType(scenarioType);
    }
    @Override
    default void exitAutosort(SppParser.AutosortContext ctx) {
    }

    @Override
    default void enterExecutegroup(SppParser.ExecutegroupContext ctx){
        SppScenario sppScenario = (SppScenario) getSppDomain().getCurrentClass();
        ExecuteBlock executeBlock = createExecuteBlock(sppScenario.getScenarioType());

        push(executeBlock);
    }

    @Override
    default void exitExecutegroup(SppParser.ExecutegroupContext ctx){
        ExecuteBlock executeBlock = (ExecuteBlock) pop();
    }

    @Override
    default void enterSerialgroup(@NotNull SppParser.SerialgroupContext ctx){
        ExecuteBlock executeBlock = createExecuteBlock(ScenarioType.serial);

        push(executeBlock);
    }

    @Override
    default void exitSerialgroup(@NotNull SppParser.SerialgroupContext ctx){
        ExecuteBlock executeBlock = (ExecuteBlock) pop();
    }

    @Override
    default void enterParallelgroup(@NotNull SppParser.ParallelgroupContext ctx){
        ExecuteBlock executeBlock = createExecuteBlock(ScenarioType.parallel);

        push(executeBlock);
    }
    @Override
    default void exitParallelgroup(@NotNull SppParser.ParallelgroupContext ctx){
        ExecuteBlock executeBlock = (ExecuteBlock) pop();
    }

    @Override
    default void enterTransactionStatements(SppParser.TransactionStatementsContext ctx) {
        ServiceBaseBody serviceBaseBody = getCurrentServiceBaseBody();
        TransactionBlock transactionBlock = new TransactionBlock();

        serviceBaseBody.addServiceCall(transactionBlock);
        push(transactionBlock);
    }

    @Override
    default void exitTransactionStatements(SppParser.TransactionStatementsContext ctx) {
        TransactionBlock serviceBaseBody = (TransactionBlock) pop();
    }

    @Override
    default void enterAtomiccall(SppParser.AtomiccallContext ctx) {
        String serviceName = ctx.getChild(0).getText();
        String serviceClassName = NameUtil.firstToLowerCase(serviceName,false);
        SppDomain checker = checkClass(ctx,serviceClassName);
        SppService currentCLass = (SppService) checker.getCurrentClass();
        SppServiceCall sppServiceCall = new SppServiceCall();
        getCurrentServiceBaseBody().addServiceCall(sppServiceCall);

        SppClass callee = (SppClass) checker.getSppClass(CompilationUnitType.atomicservice,serviceClassName);
        if(callee instanceof SppService) {
            sppServiceCall.setCallee((SppService) callee);
            currentCLass.setCurrentCall(new CurrentCall((SppService) callee,0));
        }else if(!callee.isReal()){
            checker.addUnFoundClass(new ErrorContent(serviceClassName,
                    ctx.getStart().getLine(),ctx.getStart().getCharPositionInLine(),
                    "Callee:"+serviceClassName+" is not  exists!"));
//            logSppError(ctx,"Callee:"+serviceClassName+" is not  exists!");
        }
        else
            logSppError(ctx,"Callee:"+serviceClassName+" is not a service!");
        String err = generateField(serviceClassName,0,serviceName);
        logSppError(ctx,err);
    }

    @Override
    default void exitAtomiccall(SppParser.AtomiccallContext ctx) {
    }
    @Override
    default void enterParameters(SppParser.ParametersContext ctx) {
    }

    @Override
    default void exitParameters(SppParser.ParametersContext ctx) {
    }

    @Override
    default void enterParameter(SppParser.ParameterContext ctx) {
        String name = ctx.getChild(0).getText();
        String paramName = createAParameter(name);
        ServiceBaseBody serviceBaseBody = getCurrentServiceBaseBody();
        //checkQualifiedName
        String err = serviceBaseBody.checkQualifiedName(paramName);
        logSppError(ctx,err);
        SppClass currentCLass = (SppClass) getSppDomain().getCurrentClass();
        //check call parameter type and position
        CurrentCall currentCall = currentCLass.getCurrentCall();
        //callee's n parameter
//        System.out.println("Callee "+currentCall.callee().getName() +" get parameter " + currentCall.paramIndex);
        SppLocalVar indexVar = currentCall.callee().getServiceBody().getLocalVar(currentCall.paramIndex());
        //local var type
        SppLocalVar nameVar = serviceBaseBody.getQualifieField(paramName);
        SppServiceCall sppServiceCall = (SppServiceCall) serviceBaseBody.getLastServiceCall();
        sppServiceCall.addParameter( new SppParameter(nameVar == null ? null: nameVar.getType(),paramName)
                .setArrayDimension(nameVar.getArrayDimension()));
        if(nameVar == null ){
            logSppError(ctx, "Parameter " + name +" is  null ");
        }else if(!isSameTypeWithParameter(indexVar, nameVar)){
            logSppError(ctx, "Parameter " + name +" is  excepted " +indexVar.getType().getName() +(indexVar.getArrayString()));
        }
        currentCLass.setCurrentCall(new CurrentCall(currentCall.callee,currentCall.paramIndex+1));
    }

    private static boolean isSameTypeWithParameter(SppLocalVar indexVar, SppLocalVar nameVar) {
        boolean isSame = indexVar.getType().getName().equals(nameVar.getType().getName());
        isSame = isSame && indexVar.getArrayDimension() == nameVar.getArrayDimension();
        return isSame;
    }

    @Override
    default void exitParameter(SppParser.ParameterContext ctx) {
    }

    @Override
    default void exitScenarioDeclaration(SppParser.ScenarioDeclarationContext ctx) {
    }

    record CurrentCall(SppService callee, int paramIndex){

    }
    private ServiceBaseBody getCurrentServiceBaseBody() {
        SppScenario currentCLass = (SppScenario) getSppDomain().getCurrentClass();
        ServiceBaseBody serviceBaseBody = currentCLass.getServiceBody();
        if(stackSize() != 0 && peek() instanceof ServiceBaseBody){
            serviceBaseBody = (ServiceBaseBody) peek();
        }
        return serviceBaseBody;
    }

    private ExecuteBlock createExecuteBlock(ScenarioType scenarioType) {
        ServiceBaseBody serviceBaseBody = getCurrentServiceBaseBody();
        ExecuteBlock executeBlock = new ExecuteBlock();
        executeBlock.setScenarioType(scenarioType);

        serviceBaseBody.addServiceCall(executeBlock);
        return executeBlock;
    }
}
