package free.servpp.generator;

import free.servpp.SppParser;
import free.servpp.generator.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Stack;

/**
 * @author lidong@date 2023-10-31@version 1.0
 */
public class Spp2JavaListener extends CompilationUnitGenerator{
    private File genRoot;
    private String javaPackege;
    public Spp2JavaListener(File genRoot) {
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