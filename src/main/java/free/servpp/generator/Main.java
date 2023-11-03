package free.servpp.generator;

import free.servpp.SppLexer;
import free.servpp.SppParser;
import free.servpp.generator.models.ErrorContent;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.util.List;

/**
 * @author lidong@date 2023-10-30@version 1.0
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String genPath = "/Users/lidong/gitspace/spplang/target/gen/spp/";
        String file = "/Users/lidong/gitspace/spplang/src/main/resources/spps/invoice.spp";
        FileInputStream reader = new FileInputStream(file);
        byte[] bytes = reader.readAllBytes();
        reader.close();;
        String sql = new String(bytes);
        CharStream stream = CharStreams.fromString(sql);
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

        // 创建我们自定义的监听器
        Spp2JavaListener listener = new Spp2JavaListener(new File(genPath));
        listener.setSppFile(file);

        walker.walk(listener, tree);
        List<ErrorContent> undefClasses = listener.getClassChecker().checkAll();
        for(ErrorContent cont: undefClasses){
            listener.logSppError(cont.getLine(), cont.getCol(), cont.getMsg());
        }
        new AllClassGenerator().generate(listener.getClassChecker(), listener.getDomainPath(),listener.getJavaPackege());
    }
}
