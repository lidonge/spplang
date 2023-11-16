package free.servpp.generator.java;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.IFileGenerator;
import free.servpp.generator.models.SppClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public interface IClassWriter extends IFileGenerator {
    String getJavaPackage();
    String getBasePackage();
    default void genClassDefine(SppClass sppClass) throws FileNotFoundException {
        genClassDefine(sppClass,null);
    }
    default void genClassDefine(SppClass sppClass, String addtionalPackage) throws FileNotFoundException {
        setBraceLevel(0);
        String objName = sppClass.getName();
        File clsPath = new File(getDomainPath(), addtionalPackage == null ? "" : addtionalPackage );
        if(!clsPath.exists()){
            clsPath.mkdirs();
        }
        File objPath = new File(clsPath, objName + IConstance.SUFFIX);

        FileOutputStream fout = new FileOutputStream(objPath);
        setPrintStream( new PrintStream(fout));
//            PrintStream out = System.out;
        println("package " + getBasePackage() + "."+getJavaPackage() + (addtionalPackage == null ? "" : "." +addtionalPackage)+";");
        IConstance.CompilationUnitType type = sppClass.getType();
        print("public " + typeToString(type) + " " + objName);
        printlnFromBeg((type == IConstance.CompilationUnitType.atomicservice ? " extends " : " implements ") + typeToExtendString(type) + " {");
        intoBrace();
    }
}
