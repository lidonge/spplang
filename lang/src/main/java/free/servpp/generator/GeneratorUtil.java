package free.servpp.generator;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.generator.general.SppGeneralHandler;
import free.servpp.generator.general.app.AppGeneralHandler;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.SppProject;
import free.servpp.generator.openapi.CodeFormator;
import free.servpp.generator.openapi.OpenApiGenerator;
import io.swagger.v3.oas.models.OpenAPI;

import java.io.*;

/**
 * @author lidong@date 2023-10-30@version 1.0
 */
public class GeneratorUtil {
    public static void main(String[] args) throws IOException {
        InputStream mustache = GeneratorUtil.class.getResourceAsStream("/spp.mustache");
        SppProject sppProject = new SppProject();
        File sppFile = new File("/Users/lidong/gitspace/spplang/lang/src/main/spp/invoice.spp");
        File appFile = new File("/Users/lidong/gitspace/spplang/lang/src/main/spp/invoice.app");
        File yamlOutPath = new File("/Users/lidong/gitspace/spplang/lang/target/gen");
        File genRoot = new File("/Users/lidong/gitspace/spplang/lang/target/gen/src/java");
        String basePackage = "free.servpp.openapi";
        SppGeneralHandler sppGeneralHandler = (SppGeneralHandler) new SppCompiler(sppFile,sppProject).compile();

        SppDomain sppDomain = sppGeneralHandler.getSppDomain();
        sppDomain.dealMaps();
        AppGeneralHandler appGeneralHandler = (AppGeneralHandler) new AppCompiler(appFile,sppProject).compile();
        openApi(sppDomain,mustache, sppFile,yamlOutPath, genRoot, basePackage);
    }
    public static void openApi(SppDomain sppDomain, InputStream mustache, File sppFile, File yamlOutPath,
                                File javaGenRoot, String basePackage) throws IOException {
        String domainName = sppDomain.getName();
        File domainPath = new File(new File(javaGenRoot, basePackage.replaceAll("\\.",File.separator)), domainName);
        if (!domainPath.exists())
            domainPath.mkdirs();
        OpenApiGenerator openApiGenerator = new OpenApiGenerator();
        OpenAPI openAPI = openApiGenerator.generate(sppDomain,
                domainPath, basePackage);
        Template template = Mustache.compiler().compile(new InputStreamReader(mustache));
        if(!yamlOutPath.exists())
            yamlOutPath.mkdirs();
        File outFile = new File(yamlOutPath, sppFile.getName() + ".yaml");
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(outFile));
//            out = new PrintWriter(System.out);
            out.println(CodeFormator.formatCode(template.execute(openAPI)));
        }finally {
            out.close();
        }
        openApiGenerator.createJavaSpringMustache(yamlOutPath);
        openApiGenerator.createServicesProperties(javaGenRoot);
    }
}
