package free.servpp.generator.java;

import free.servpp.generator.general.BaseClassGenerator;
import free.servpp.generator.models.*;

import java.io.File;
import java.util.Map;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class AllClassGenerator extends BaseClassGenerator {
    public void generate(SppDomain sppDomain, File domainPath, String basePackage, String javaPackege){
        dealMaps(sppDomain);
        Map<String, SppClass> sppClassMap = sppDomain.getMapsOfClass();
        for(SppClass sppClass: sppClassMap.values()){
//            if(sppClass.getType() == IConstance.CompilationUnitType.rolemapper)
//                System.out.println(sppClass);
            new ClassWriter(domainPath,sppClass,basePackage,javaPackege).generate();
        }
    }
}
