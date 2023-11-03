package free.servpp.generator;

import free.servpp.SppParser;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IAtomicServiceGenerator extends IServiceDefinitionGenerator {
    @Override
    default void enterAtomicservice(SppParser.AtomicserviceContext ctx) {
        push(CompilationUnitType.atomicservice);
    }

    @Override
    default void exitAtomicservice(SppParser.AtomicserviceContext ctx) {

    }

    @Override
    default void enterServicetype(SppParser.ServicetypeContext ctx){

    }
    @Override
    default void exitServicetype(SppParser.ServicetypeContext ctx){
        String stype = ctx.getChild(0).getText();
        push(ServiceType.valueOf(stype));
    }

}
