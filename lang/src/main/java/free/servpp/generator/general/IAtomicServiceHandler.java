package free.servpp.generator.general;


import free.servpp.generator.models.ServiceBody;
import free.servpp.generator.models.SppService;
import free.servpp.lang.antlr.SppParser;

import java.util.List;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IAtomicServiceHandler extends IServiceDefinitionHandler {
    @Override
    default void enterAtomicservice(SppParser.AtomicserviceContext ctx) {
        SppService currentClass = new SppService("");
        currentClass.setType(CompilationUnitType.atomicservice);
        getSppDomain().setCurrentClass(currentClass);

//        push(CompilationUnitType.atomicservice);
    }

    @Override
    default void exitAtomicservice(SppParser.AtomicserviceContext ctx) {
        getSppDomain().setCurrentClass(null);
    }

    @Override
    default void enterServicetype(SppParser.ServicetypeContext ctx){
        String stype = ctx.getChild(0).getText();
        SppService service = (SppService) getSppDomain().getCurrentClass();
        service.setServiceType(ServiceType.valueOf(stype));
    }
    @Override
    default void exitServicetype(SppParser.ServicetypeContext ctx){
//        push(ServiceType.valueOf(stype));
    }

    @Override
    default void enterReturnList(SppParser.ReturnListContext ctx){
        SppService service = (SppService) getSppDomain().getCurrentClass();
        push(service.getReturns());
    }

    @Override
    default void exitReturnList(SppParser.ReturnListContext ctx){
        ServiceBody serviceBody = (ServiceBody) pop();
    }

    @Override
    default void enterReturnType(SppParser.ReturnTypeContext ctx){

    }

    @Override
    default void exitReturnType(SppParser.ReturnTypeContext ctx){

    }
}
