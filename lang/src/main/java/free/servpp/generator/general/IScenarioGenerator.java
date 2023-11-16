package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppParser;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IScenarioGenerator extends IServiceDefinitionGenerator {
    @Override
    default void enterScenario(SppParser.ScenarioContext ctx) {
        push(CompilationUnitType.scenario);
    }

    @Override
    default void exitScenario(SppParser.ScenarioContext ctx) {
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
        ServiceBaseBody serviceBaseBody = (ServiceBaseBody) pop();
    }

    @Override
    default void enterAtomiccall(SppParser.AtomiccallContext ctx) {
        String serviceName = ctx.getChild(0).getText();
        String serviceClassName = IConstance.firstToLowerCase(serviceName,false);
        ClassChecker checker = checkClass(ctx,serviceClassName);
        ServiceBaseBody serviceBaseBody = getCurrentServiceBaseBody();
        SppService currentCLass = (SppService) checker.getCurrentClass();
        SppServiceCall sppServiceCall = createServiceCall(ctx);
        SppClass callee = checker.getSppClass(CompilationUnitType.atomicservice,serviceClassName);
        if(callee instanceof SppService) {
            sppServiceCall.setCallee((SppService) callee);
            currentCLass.setCurrentCall(new CurrentCall(callee,0));
        }else if(!callee.isReal()){
            checker.addUnFoundClass(new ErrorContent(serviceClassName,
                    ctx.getStart().getLine(),ctx.getStart().getCharPositionInLine(),
                    "Callee:"+serviceClassName+" is not  exists!"));
//            logSppError(ctx,"Callee:"+serviceClassName+" is not  exists!");
        }
        else
            logSppError(ctx,"Callee:"+serviceClassName+" is not a service!");
        String err = generateField(serviceClassName,serviceName);
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
        SppClass currentCLass = getClassChecker().getCurrentClass();
        //check call parameter type and position
        CurrentCall currentCall = currentCLass.getCurrentCall();
        //callee's n parameter
//        System.out.println("Callee "+currentCall.callee().getName() +" get parameter " + currentCall.paramIndex);
        SppLocalVar indexVar = currentCall.callee().getField(currentCall.paramIndex());
        //local var type
        SppClass nameVar = serviceBaseBody.getQualifieField(paramName);
        SppServiceCall sppServiceCall = (SppServiceCall) serviceBaseBody.getLastServiceCall();
        sppServiceCall.addParameter( new SppParameter(nameVar == null ? null: nameVar,paramName));
        if(nameVar == null ){
            logSppError(ctx, "Parameter " + name +" is  null ");
        }else if(!indexVar.getType().getName().equals(nameVar.getName())){
            logSppError(ctx, "Parameter " + name +" is  excepted " +indexVar.getType().getName());
        }
        currentCLass.setCurrentCall(new CurrentCall(currentCall.callee,currentCall.paramIndex+1));
    }

    @Override
    default void exitParameter(SppParser.ParameterContext ctx) {
    }

    @Override
    default void exitScenarioDeclaration(SppParser.ScenarioDeclarationContext ctx) {
    }

    record CurrentCall(SppClass callee, int paramIndex){

    }
    private SppServiceCall createServiceCall(ParserRuleContext ctx) {
        SppServiceCall sppServiceCall = new SppServiceCall();
        getCurrentServiceBaseBody().addServiceCall(sppServiceCall);
        return sppServiceCall;
    }

    private ServiceBaseBody getCurrentServiceBaseBody() {
        SppClass currentCLass = getClassChecker().getCurrentClass();
        ServiceBaseBody serviceBaseBody = currentCLass.getServiceBody();
        if(stackSize() != 0 && peek() instanceof ServiceBaseBody){
            serviceBaseBody = (ServiceBaseBody) peek();
        }
        return serviceBaseBody;
    }

}
