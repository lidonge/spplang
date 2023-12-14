package free.servpp.generator.java;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppService;

import java.io.FileNotFoundException;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public interface IAtomicServiceWriter extends IServiceWriter{
    default void genAtomicService(SppClass sppClass) throws FileNotFoundException {
        IConstance.CompilationUnitType type =sppClass.getType();
        genClassDefine(sppClass);
        genServiceDefinition(type, (SppService) sppClass);
        println(";");
        println("}");
    }
}
