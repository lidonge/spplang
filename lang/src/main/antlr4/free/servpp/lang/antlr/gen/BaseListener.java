// Generated from /Users/lidong/gitspace/spplang/lang/src/main/antlr4/free/servpp/lang/antlr/Base.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BaseParser}.
 */
public interface BaseListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BaseParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(BaseParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(BaseParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(BaseParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(BaseParser.PrimitiveTypeContext ctx);
}