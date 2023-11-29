package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppParser;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IServiceDefinitionHandler extends ICompilationUnitHandler {
    @Override
    default void enterServiceDefinition(SppParser.ServiceDefinitionContext ctx) {
        ScenarioType scenarioType = null;
        if(peek() instanceof ScenarioType)
            scenarioType = (ScenarioType) pop();
        ServiceType serviceType = null;
        if(peek() instanceof ServiceType)
            serviceType = (ServiceType) pop();
        CompilationUnitType type = (CompilationUnitType) pop();
        String methodName = ctx.getChild(0).getText();
        String clsName = NameUtil.firstToLowerCase(methodName,false);
        SppService sppService = generateService(type, ctx, clsName,methodName);
        sppService.setServiceType(serviceType);
        if(sppService instanceof SppScenario){
            ((SppScenario)sppService).setScenarioType(scenarioType);
        }
    }

    @Override
    default void exitServiceDefinition(SppParser.ServiceDefinitionContext ctx) {
    }

    @Override
    default void enterParameterDeclarations(SppParser.ParameterDeclarationsContext ctx) {
        push(true);
    }

    @Override
    default void exitParameterDeclarations(SppParser.ParameterDeclarationsContext ctx) {
        boolean isFirst = (boolean) pop();
    }

    @Override
    default void enterParameterDeclaration(SppParser.ParameterDeclarationContext ctx) {
        int count = ctx.getChildCount();
        String ptype = ctx.getChild(0).getText();
        String name = count == 1 ? null : ctx.getChild(1).getText();

        String[] sdims = ptype.split("\\[");
        String paramName = createAParameter(sdims[0], name);
        SppDomain checker = checkClass(ctx,sdims[0]);
//        String err = checker.getCurrentClass().addLocalVar(new SppLocalVar(checker.getSppClass(ptype),paramName));
        CompilationUnitType unitType = checker.getCurrentClass().getType();
        int dim = sdims.length -1;
        String err = unitType == CompilationUnitType.scenario?
                generateLocalVar(sdims[0],dim, paramName) :
                generateField(sdims[0],dim,paramName);
        logSppError(ctx,err);

    }

}
