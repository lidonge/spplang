package free.servpp.generator.general;

import free.servpp.lang.antlr.SppParser;

import java.io.File;

/**
 * @author lidong@date 2023-10-31@version 1.0
 */
public class SppGeneralListener extends CompilationUnitGenerator {
    private File genRoot;
    private String javaPackage;
    private String basePackage;
    public SppGeneralListener(File genRoot,String basePackage) {
        this.genRoot = genRoot;
        this.basePackage = basePackage;
    }

    public File getGenRoot() {
        return genRoot;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public String getBasePackage() {
        return basePackage;
    }

    @Override
    public void enterDomainname(SppParser.DomainnameContext ctx) {
        String domainName = ctx.getText();
        this.javaPackage = domainName;
        this.domainPath = new File(new File(genRoot, basePackage.replaceAll("\\.",File.separator)), domainName);
//        System.out.println("basePackage:"+basePackage );
//        System.out.println("domainPath:"+domainPath );
        if (!domainPath.exists())
            domainPath.mkdirs();
       push(domainName);
    }

}