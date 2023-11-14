package free.servpp.generator;

import com.github.mustachejava.*;
import com.github.mustachejava.codes.NotIterableCode;
import free.servpp.generator.general.GeneratorErrorListener;
import free.servpp.generator.general.SppGeneralListener;
import free.servpp.generator.models.ErrorContent;
import free.servpp.generator.openapi.MapObjectHandler;
import free.servpp.generator.openapi.MyIterableCode;
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
import java.util.List;

/**
 * @author lidong@date 2023-10-30@version 1.0
 */
public class Compiler {
    static final String HEADER = "%Header=";
    public static void main(String[] args) throws IOException {
        String genPath = "/Users/lidong/gitspace/spplang/lang/target/spp/";
        String file = "/Users/lidong/gitspace/spplang/lang/src/main/resources/spps/invoice.spp";
        if(args.length == 2){
            genPath = args[0];
            file = args[1];
        }
        compile(new File(file), new File(genPath));
    }

    public static void compile(File file, File genPath) throws IOException {
        FileInputStream reader = new FileInputStream(file);
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
        parser1.addErrorListener(new GeneratorErrorListener(file));

        ParseTree tree = null;
        try {
            tree = parser1.program(); // STAGE 1
//            System.out.println(tree.toStringTree());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ParseTreeWalker walker = new ParseTreeWalker();

        SppGeneralListener listener = new SppGeneralListener(genPath);
        listener.setSppFile(file);

        walker.walk(listener, tree);
        List<ErrorContent> undefClasses = listener.getClassChecker().checkAll();
        for(ErrorContent cont: undefClasses){
            listener.logSppError(cont.getLine(), cont.getCol(), cont.getMsg());
        }
        openApi(listener,file,genPath);
    }

    private static void openApi(SppGeneralListener listener, File file, File genPath) throws IOException {
        OpenAPI openAPI = new OpenApiGenerator().generate(listener.getClassChecker(),
                listener.getDomainPath(), listener.getJavaPackege());
        DefaultMustacheFactory mf = new DefaultMustacheFactory(){
            @Override
            public MustacheVisitor createMustacheVisitor() {
                return new DefaultMustacheVisitor(this){
                    @Override public void iterable(TemplateContext templateContext, String variable, Mustache mustache){
                        String[] vars = header(variable);
                        list.add(new MyIterableCode(vars[1],templateContext, df, mustache, vars[0]));
                    }
                };
            }

        };
        mf.setObjectHandler(new MapObjectHandler());
        Mustache mustache = mf.compile("spp.mustache");
        File outFile = new File(genPath, file.getName() + ".yaml");
        mustache.execute(new PrintWriter(new FileOutputStream(outFile)),openAPI).flush();
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
