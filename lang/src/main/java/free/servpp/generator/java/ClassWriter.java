package free.servpp.generator.java;

import free.servpp.generator.models.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class ClassWriter extends BaseClassWriter implements IRoleMapperWriter, IServiceWriter, IEntityWriter, IScenarioWriter, IAtomicServiceWriter {

    public ClassWriter(File domainPath, SppClass sppClass, String basepackage, String javaPackage) {
        super(domainPath, sppClass, basepackage, javaPackage);
    }

    public void generate() {
        try {
            CompilationUnitType type = sppClass.getType();
            if (type != null) {
                if (type == CompilationUnitType.role | type == CompilationUnitType.entity |
                        type == CompilationUnitType.reference) {
//                    printlnFromBeg(" implements " + typeToExtendString(type) + " {");
//                    intoBrace();
                    genObject(sppClass);
                } else if (type == CompilationUnitType.rolemapper) {
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
