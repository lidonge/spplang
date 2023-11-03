package free.servpp.generator;

import free.servpp.SppParser;
import free.servpp.generator.models.*;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IServiceDefinitionGenerator extends ICompilationUnitGenerator{
    @Override
    default void enterServiceDefinition(SppParser.ServiceDefinitionContext ctx) {
        ServiceType serviceType = null;
        if(peek() instanceof ServiceType)
            serviceType = (ServiceType) pop();
        CompilationUnitType type = (CompilationUnitType) pop();
        String methodName = ctx.getChild(0).getText();
        String clsName = firstToLowerCase(methodName,false);
        SppService sppService = generateService(type, ctx, clsName,methodName);
        sppService.setServiceType(serviceType);
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

        String paramName = createAParameter(ptype, name);
        ClassChecker checker = checkClass(ctx,ptype);
//        String err = checker.getCurrentClass().addLocalVar(new SppLocalVar(checker.getSppClass(ptype),paramName));
        CompilationUnitType unitType = checker.getCurrentClass().getType();
        String err = unitType == CompilationUnitType.scenario? generateLocalVar(ptype, paramName) : generateField(ptype,paramName);
        logSppError(ctx,err);

    }

}
