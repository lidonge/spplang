package free.servpp.generator.openapi;

import free.servpp.generator.java.BaseClassWriter;
import free.servpp.generator.java.IAtomicServiceWriter;
import free.servpp.generator.java.IRoleMapperWriter;
import free.servpp.generator.java.IScenarioWriter;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppRoleMapper;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public class OpenAPIDirectClassWriter extends BaseClassWriter implements IAtomicServiceWriter, IScenarioWriter, IRoleMapperWriter {
    public OpenAPIDirectClassWriter(File domainPath, SppClass sppClass, String basePackage, String javaPackage) {
        super(domainPath, sppClass, basePackage, javaPackage);
    }

    public void generate() {
        try {
            SppClass sppClass = (SppClass) sppCompilationUnit;
            CompilationUnitType type = sppClass.getType();
            if (type != null) {
                if (type == CompilationUnitType.rolemapper) {
                    genRoleMapper((SppRoleMapper) sppClass);
                } else if (type == CompilationUnitType.atomicservice) {
//                    printlnFromBeg((type == CompilationUnitType.atomicservice ? " extends " : " implements ") + typeToExtendString(type) + " {");
//                    intoBrace();
                    genAtomicService(sppClass);
                } else if (type == CompilationUnitType.scenario) {
                    genScenario(sppClass);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
