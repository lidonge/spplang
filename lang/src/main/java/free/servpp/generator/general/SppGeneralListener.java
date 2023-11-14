package free.servpp.generator.general;

import free.servpp.lang.antlr.SppParser;

import java.io.File;

/**
 * @author lidong@date 2023-10-31@version 1.0
 */
public class SppGeneralListener extends CompilationUnitGenerator {
    private File genRoot;
    private String javaPackege;
    public SppGeneralListener(File genRoot) {
        this.genRoot = genRoot;
    }

    public String getJavaPackege() {
        return javaPackege;
    }

    @Override
    public void enterDomainname(SppParser.DomainnameContext ctx) {
        String domainName = ctx.getText();
        this.javaPackege = domainName;
//        System.out.println("enterDomainname:"+domainName);
        this.domainPath = new File(genRoot, domainName);
        if (!domainPath.exists())
            domainPath.mkdirs();
       push(domainName);
    }

}