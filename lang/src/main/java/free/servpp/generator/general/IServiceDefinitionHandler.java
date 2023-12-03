package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppParser;

import java.util.List;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IServiceDefinitionHandler extends ICompilationUnitHandler {
    @Override
    default void enterServiceDefinition(SppParser.ServiceDefinitionContext ctx) {
        String methodName = ctx.getChild(0).getText();
        String clsName = NameUtil.firstToLowerCase(methodName,false);
        SppService sppService = (SppService) getSppDomain().getCurrentClass();
        sppService.setName(clsName);
        sppService.setFuncName(methodName);
        String err = getSppDomain().checkDupClass(clsName);
        if(err != null){
            logSppError(ctx,err);
        }
        getSppDomain().addClass(sppService);
        push(sppService.getServiceBody());

    }

    @Override
    default void exitServiceDefinition(SppParser.ServiceDefinitionContext ctx) {
        ServiceBody serviceBody = (ServiceBody) pop();
    }

    @Override
    default void enterParameterDeclarations(SppParser.ParameterDeclarationsContext ctx) {

    }

    @Override
    default void exitParameterDeclarations(SppParser.ParameterDeclarationsContext ctx) {
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
        SppLocalVar sppLocalVar = generateLocalVar(sdims[0],dim, paramName);
        Object container = peek();
        if(container instanceof ServiceBody){
            ServiceBody serviceBody = (ServiceBody) container;
            String err = serviceBody.addLocalVar(sppLocalVar);
            logSppError(ctx,err);
        }

    }

}
