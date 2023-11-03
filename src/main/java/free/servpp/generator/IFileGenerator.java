package free.servpp.generator;

import java.io.File;
import java.io.PrintStream;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public interface IFileGenerator extends IConstance{
    void setBraceLevel(int i);
    void incBraceLevel();

    void decBraceLevel();

    int getBraceLevel();

    PrintStream getPrintStream();
    File getDomainPath();
    default String typeToString(CompilationUnitType type) {
        String ret = null;
        switch (type) {

            case entity -> {
                ret = "class";
            }
            case role -> {
                ret = "class";
            }
            case actas -> {
            }
            case atomicservice -> {
                ret = "interface";
            }
            case scenario -> {
                ret = "class";
            }
            case rolemapper -> {
                ret = "class";
            }
        }
        return ret;
    }

    default String typeToExtendString(CompilationUnitType type) {
        String ret = null;
        switch (type) {

            case entity -> {
                ret = compilationUnitPackage + ".Entity";
            }
            case reference -> {
                ret = compilationUnitPackage + ".Reference";
            }
            case role -> {
                ret = compilationUnitPackage + ".Role";
            }
            case actas -> {
            }
            case atomicservice -> {
                ret = compilationUnitPackage + ".AtomicService";
            }
            case scenario -> {
                ret = compilationUnitPackage + ".Scenario";
            }
            case rolemapper -> {
                ret = compilationUnitPackage + ".RoleMapper";
            }
        }
        return ret;
    }

    public static String toJavaPrimaryType(String spptype){
        if("date".equals(spptype))
            return "java.util.Date";
        return spptype;
    }
    default void intoBrace() {
        incBraceLevel();
    }

    default void printTab(PrintStream out) {
        int braceLevel = getBraceLevel();
        for (int i = 0; i < braceLevel; i++)
            out.print("\t");
    }

    default void outBrace() {
        decBraceLevel();
    }

    default void printlnFromBeg(String s) {
        PrintStream out = getPrintStream();
        printTab(out);
        out.println(s);
    }

    default void printFromBeg(String s) {
        PrintStream out = getPrintStream();
        printTab(out);
        out.print(s);
    }

    default void println(String s) {
        PrintStream out = getPrintStream();
        out.println(s);
    }

    default void print(String s) {
        PrintStream out = getPrintStream();
        out.print(s);
    }

    default void outFile() {
        PrintStream out = getPrintStream();
        out.close();
    }

}
