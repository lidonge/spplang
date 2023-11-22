package free.servpp.generator;

import free.servpp.generator.general.app.AppGeneralHandler;
import free.servpp.generator.models.SppProject;
import free.servpp.lang.antlr.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import java.io.File;
import java.io.IOException;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class AppCompiler extends AntlrCompiler{
    SppProject sppProject;
    public AppCompiler(File antlrFile, SppProject sppProject) {
        super(antlrFile);
        this.sppProject = sppProject;
    }

    @Override
    protected Parser getParser() throws IOException {
        CharStream stream = getCharStream();
        AppLexer lexer1 = new AppLexer(stream);
        CommonTokenStream tokens1 = new CommonTokenStream(lexer1);
        AppParser parser1 = new AppParser(tokens1);
        return parser1;
    }

    @Override
    public ParseTreeListener compile() throws IOException {
        AppParser appParser = (AppParser) getParser();
        AppGeneralHandler listener = new AppGeneralHandler(antlrFile, sppProject);

        parseFile(appParser, ()->appParser.program(),listener);
        return listener;
    }
}
