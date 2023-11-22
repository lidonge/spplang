package free.servpp.generator;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.generator.general.SppGeneralHandler;
import free.servpp.generator.general.app.AppGeneralHandler;
import free.servpp.generator.models.SppProject;
import free.servpp.generator.openapi.OpenApiGenerator;
import io.swagger.v3.oas.models.OpenAPI;

import java.io.*;

/**
 * @author lidong@date 2023-10-30@version 1.0
 */
public class Compiler {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Compiler.class.getResourceAsStream("/spp.mustache");
        SppProject sppProject = new SppProject();
        Compiler.compileAndGen(inputStream,
                new File("/Users/lidong/gitspace/spplang/lang/src/main/spp/invoice.spp"),
                new File("/Users/lidong/gitspace/spplang/lang/src/main/spp/invoice.app"),
                new File("/Users/lidong/gitspace/spplang/lang/target/gen"),
                new File("/Users/lidong/gitspace/spplang/lang/target/gen/src/java"), "free.servpp.openapi",sppProject);
    }

    public static void compileAndGen(InputStream mustache, File sppFile, File appFile,
            File yamlOutPath, File genRoot, String basePackage, SppProject sppProject) throws IOException {
        SppGeneralHandler sppGeneralHandler = (SppGeneralHandler) new SppCompiler(sppFile,sppProject).compile();
        AppGeneralHandler appGeneralHandler = (AppGeneralHandler) new AppCompiler(appFile,sppProject).compile();
        String domainName = sppGeneralHandler.getSppDomain().getName();
        System.out.print(sppProject.getDomain(domainName).getRuleBlock());
        File domainPath = new File(new File(genRoot, basePackage.replaceAll("\\.",File.separator)), domainName);
        openApi(sppGeneralHandler,mustache, sppFile,yamlOutPath, genRoot, basePackage,domainPath,domainName);
    }

    private static void openApi(SppGeneralHandler listener, InputStream mustache, File sppFile, File yamlOutPath,
                                File genRoot, String basePackage, File domainPath, String domainName) throws IOException {
        if (!domainPath.exists())
            domainPath.mkdirs();
        OpenApiGenerator openApiGenerator = new OpenApiGenerator();
        OpenAPI openAPI = openApiGenerator.generate(listener.getSppDomain(),
                domainPath, basePackage, domainName);
        Template template = Mustache.compiler().compile(new InputStreamReader(mustache));
        if(!yamlOutPath.exists())
            yamlOutPath.mkdirs();
        File outFile = new File(yamlOutPath, sppFile.getName() + ".yaml");
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(outFile));
//            out = new PrintWriter(System.out);
            out.println(template.execute(openAPI));
        }finally {
            out.close();
        }
        openApiGenerator.createJavaSpringMustache(yamlOutPath);
        openApiGenerator.createServicesProperties(genRoot);
    }
}
