// Generated from /Users/lidong/gitspace/spplang/lang/src/main/antlr4/free/servpp/lang/antlr/JavaExpr.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JavaExprParser}.
 */
public interface JavaExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code logicOrExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicOrExpr(JavaExprParser.LogicOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicOrExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicOrExpr(JavaExprParser.LogicOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimExpr(JavaExprParser.PrimExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimExpr(JavaExprParser.PrimExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayExpr(JavaExprParser.ArrayExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayExpr(JavaExprParser.ArrayExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rightIncDecExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterRightIncDecExpr(JavaExprParser.RightIncDecExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rightIncDecExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitRightIncDecExpr(JavaExprParser.RightIncDecExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPlusMinusExpr(JavaExprParser.PlusMinusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPlusMinusExpr(JavaExprParser.PlusMinusExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code questionExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterQuestionExpr(JavaExprParser.QuestionExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code questionExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitQuestionExpr(JavaExprParser.QuestionExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesesExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesesExpr(JavaExprParser.ParenthesesExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesesExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesesExpr(JavaExprParser.ParenthesesExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code devMultModeExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDevMultModeExpr(JavaExprParser.DevMultModeExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code devMultModeExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDevMultModeExpr(JavaExprParser.DevMultModeExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitXorExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitXorExpr(JavaExprParser.BitXorExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitXorExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitXorExpr(JavaExprParser.BitXorExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitAndLogicNotExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitAndLogicNotExpr(JavaExprParser.BitAndLogicNotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitAndLogicNotExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitAndLogicNotExpr(JavaExprParser.BitAndLogicNotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code shiftExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpr(JavaExprParser.ShiftExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code shiftExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpr(JavaExprParser.ShiftExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitOrExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitOrExpr(JavaExprParser.BitOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitOrExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitOrExpr(JavaExprParser.BitOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicAndExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicAndExpr(JavaExprParser.LogicAndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicAndExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicAndExpr(JavaExprParser.LogicAndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicGreatLessExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicGreatLessExpr(JavaExprParser.LogicGreatLessExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicGreatLessExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicGreatLessExpr(JavaExprParser.LogicGreatLessExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicEqualsExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicEqualsExpr(JavaExprParser.LogicEqualsExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicEqualsExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicEqualsExpr(JavaExprParser.LogicEqualsExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code leftIncDecAndSignExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLeftIncDecAndSignExpr(JavaExprParser.LeftIncDecAndSignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code leftIncDecAndSignExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLeftIncDecAndSignExpr(JavaExprParser.LeftIncDecAndSignExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitAndExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitAndExpr(JavaExprParser.BitAndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitAndExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitAndExpr(JavaExprParser.BitAndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssignExpr(JavaExprParser.AssignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssignExpr(JavaExprParser.AssignExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExprParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(JavaExprParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExprParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(JavaExprParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExprParser#primaryExprQualified}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExprQualified(JavaExprParser.PrimaryExprQualifiedContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExprParser#primaryExprQualified}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExprQualified(JavaExprParser.PrimaryExprQualifiedContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExprParser#qualified}.
	 * @param ctx the parse tree
	 */
	void enterQualified(JavaExprParser.QualifiedContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExprParser#qualified}.
	 * @param ctx the parse tree
	 */
	void exitQualified(JavaExprParser.QualifiedContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExprParser#primaryExprLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExprLiteral(JavaExprParser.PrimaryExprLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExprParser#primaryExprLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExprLiteral(JavaExprParser.PrimaryExprLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExprParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(JavaExprParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExprParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(JavaExprParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExprParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(JavaExprParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExprParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(JavaExprParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExprParser#qualifiedname}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedname(JavaExprParser.QualifiednameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExprParser#qualifiedname}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedname(JavaExprParser.QualifiednameContext ctx);
}