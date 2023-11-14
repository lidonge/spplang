package free.servpp.generator.general;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.io.File;
import java.util.BitSet;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class GeneratorErrorListener implements ANTLRErrorListener {
    File file;
    public GeneratorErrorListener(File file) {
        this.file = file;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        System.err.println(file+":" + line + ":" + (charPositionInLine+1) );
        System.err.println(msg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        System.err.println("reportAmbiguity " + startIndex + ":" + stopIndex + " " + exact);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        System.err.println("reportAttemptingFullContext " + startIndex + ":" + stopIndex );
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        System.err.println("reportContextSensitivity " + startIndex + ":" + stopIndex + " " + prediction);
    }
}
