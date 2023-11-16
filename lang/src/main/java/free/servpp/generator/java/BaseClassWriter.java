package free.servpp.generator.java;

import free.servpp.generator.general.IFileGenerator;
import free.servpp.generator.models.SppClass;

import java.io.File;
import java.io.PrintStream;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public class BaseClassWriter  extends ClassWriterConfig implements IFileGenerator, IClassWriter {
    private int braceLevel = 0;
    protected PrintStream out;

    public BaseClassWriter(File domainPath, SppClass sppClass, String basePackage, String javaPackage) {
        super(domainPath, sppClass, basePackage, javaPackage);
    }

    @Override
    public void setBraceLevel(int i) {
        braceLevel = i;
    }

    @Override
    public void incBraceLevel() {
        braceLevel++;
    }

    @Override
    public void decBraceLevel() {
        braceLevel--;
    }

    @Override
    public int getBraceLevel() {
        return braceLevel;
    }

    @Override
    public PrintStream getPrintStream() {
        return out;
    }
    @Override
    public void setPrintStream(PrintStream printStream){
        out = printStream;
    }


}
