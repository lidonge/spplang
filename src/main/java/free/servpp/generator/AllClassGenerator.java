package free.servpp.generator;

import free.servpp.generator.checker.ClassChecker;
import free.servpp.generator.checker.SppClass;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class AllClassGenerator {
    public void generate(ClassChecker classChecker, File domainPath, String javaPackege){
        Map<String, SppClass> sppClassMap = classChecker.getMapsOfClass();
        for(SppClass sppClass: sppClassMap.values()){
//            if(sppClass.getType() != null)
//                System.out.println(sppClass);
            new ClassGenerator(domainPath,sppClass,javaPackege).generate();
        }
    }

}
