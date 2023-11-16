package free.servpp.generator.java;

import free.servpp.generator.models.SppClass;

import java.io.File;
import java.io.PrintStream;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public class ClassWriterConfig {
    protected File domainPath;
    protected SppClass sppClass;
    protected String javaPackage;
    private String basePackage;
    public ClassWriterConfig(File domainPath, SppClass sppClass, String basePackage, String javaPackage) {
        this.domainPath = domainPath;
        this.sppClass = sppClass;
        this.basePackage =basePackage;
        this.javaPackage = javaPackage;
    }
    public File getDomainPath() {
        return domainPath;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public String getBasePackage() {
        return basePackage;
    }
}
