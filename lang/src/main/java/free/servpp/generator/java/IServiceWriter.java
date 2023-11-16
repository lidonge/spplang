package free.servpp.generator.java;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.IFileGenerator;
import free.servpp.generator.models.*;

import java.util.List;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public interface IServiceWriter extends IClassWriter {

    default void genServiceDefinition(IConstance.CompilationUnitType type, SppService sppService) {
//        SppService sppService = (SppService) sppClass;
        List<? extends SppLocalVar> sppLocalVars = type == IConstance.CompilationUnitType.atomicservice ? sppService.getSppFieldList()
                : sppService.getServiceBody().getSppLocalVarList();
        String retType = "void";
        if (sppService.getServiceType() == IConstance.ServiceType.check) {
            retType = "boolean";
        }
        printFromBeg("public " + retType + " " + sppService.getFuncName() + "(");
        int count = 0;
        for (SppLocalVar field : sppLocalVars) {
            if (count != 0)
                print(",");
            print(field.getType().getName() + " " + field.getName());
            count++;
        }
        print(")");
    }
}
