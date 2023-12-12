package free.servpp.generator;

import free.servpp.generator.general.SppGeneralHandler;
import free.servpp.generator.models.ErrorContent;
import free.servpp.generator.models.SppProject;
import free.servpp.lang.antlr.SppLexer;
import free.servpp.lang.antlr.SppParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import java.io.*;
import java.util.List;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class SppCompiler extends AntlrCompiler {
    SppProject sppProject;
    public SppCompiler(File sppFile,
                       SppProject sppProject) {
        super(sppFile);
        this.sppProject = sppProject;
    }

    @Override
    protected CharStream getCharStream() throws IOException {
        return super.getCharStream();
    }

    @Override
    protected Parser getParser() throws IOException {
        CharStream stream = getCharStream();
        SppLexer lexer1 = new SppLexer(stream);
        CommonTokenStream tokens1 = new CommonTokenStream(lexer1);
        SppParser parser1 = new SppParser(tokens1);
        return parser1;
    }
    @Override
    public ParseTreeListener compile() throws IOException {
        SppParser sppParser = (SppParser) getParser();
        SppGeneralHandler listener = new SppGeneralHandler(
                sppProject);
        listener.setSppFile(antlrFile);

        parseFile(sppParser, ()->{return sppParser.program();},listener);
        return listener;
    }
}
