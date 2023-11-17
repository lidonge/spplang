package free.servpp.generator;

import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Escapers;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.generator.general.GeneratorErrorListener;
import free.servpp.generator.general.SppGeneralListener;
import free.servpp.generator.models.ErrorContent;
import free.servpp.generator.openapi.OpenApiGenerator;
import free.servpp.lang.antlr.SppLexer;
import free.servpp.lang.antlr.SppParser;
import io.swagger.v3.oas.models.OpenAPI;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * @author lidong@date 2023-10-30@version 1.0
 */
public class Compiler {
    static final String HEADER = "%Header=";

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Compiler.class.getResourceAsStream("/spp.mustache");
        Compiler.compile(inputStream, new File("/Users/lidong/gitspace/spplang/lang/src/main/spp/invoice.spp"),
                new File("/Users/lidong/gitspace/spplang/lang/target/gen"),
                new File("/Users/lidong/gitspace/spplang/lang/target/gen/src/java"), "free.servpp.openapi");
    }

    public static void compile(InputStream mustache, File sppFile, File yamlOutPath, File genRoot, String basePackage) throws IOException {
        FileInputStream reader = new FileInputStream(sppFile);
        byte[] bytes = reader.readAllBytes();
        reader.close();
        ;
        String sql = new String(bytes);
        CharStream stream = new ANTLRInputStream(sql);
        SppLexer lexer1 = new SppLexer(stream);
        CommonTokenStream tokens1 = new CommonTokenStream(lexer1);
        SppParser parser1 = new SppParser(tokens1);
        parser1.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser1.removeErrorListeners();
        parser1.addErrorListener(new GeneratorErrorListener(sppFile));

        ParseTree tree = null;
        try {
            tree = parser1.program(); // STAGE 1
//            System.out.println(tree.toStringTree());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ParseTreeWalker walker = new ParseTreeWalker();

        SppGeneralListener listener = new SppGeneralListener(genRoot,basePackage);
        listener.setSppFile(sppFile);

        walker.walk(listener, tree);
        List<ErrorContent> undefClasses = listener.getClassChecker().checkAll();
        for(ErrorContent cont: undefClasses){
            listener.logSppError(cont.getLine(), cont.getCol(), cont.getMsg());
        }
        openApi(listener,mustache, sppFile,yamlOutPath);
    }

    private static void openApi(SppGeneralListener listener, InputStream mustache, File sppFile, File yamlOutPath) throws IOException {
        OpenApiGenerator openApiGenerator = new OpenApiGenerator();
        OpenAPI openAPI = openApiGenerator.generate(listener.getClassChecker(),
                listener.getDomainPath(), listener.getBasePackage(), listener.getJavaPackage());
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
        openApiGenerator.createServicesProperties(listener.getGenRoot());
    }
    private static String[] header(String var){
        int idx = var.indexOf(HEADER);
        String[] ret = new String[2];
        if(idx != -1){
            ret[1] = var.substring(idx + HEADER.length()).replace("\\n","\n");
            ret[0] = var.substring(0,idx);
        }else
            ret[0] = var;
        return ret;
    }
}
