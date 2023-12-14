// Generated from /Users/lidong/gitspace/spplang/lang/src/main/antlr4/free/servpp/lang/antlr/JavaExpr.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JavaExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JavaExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code logicOrExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicOrExpr(JavaExprParser.LogicOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimExpr(JavaExprParser.PrimExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayExpr(JavaExprParser.ArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rightIncDecExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRightIncDecExpr(JavaExprParser.RightIncDecExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusMinusExpr(JavaExprParser.PlusMinusExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code questionExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuestionExpr(JavaExprParser.QuestionExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesesExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesesExpr(JavaExprParser.ParenthesesExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code devMultModeExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDevMultModeExpr(JavaExprParser.DevMultModeExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitXorExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitXorExpr(JavaExprParser.BitXorExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitAndLogicNotExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitAndLogicNotExpr(JavaExprParser.BitAndLogicNotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code shiftExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftExpr(JavaExprParser.ShiftExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitOrExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitOrExpr(JavaExprParser.BitOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicAndExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicAndExpr(JavaExprParser.LogicAndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicGreatLessExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicGreatLessExpr(JavaExprParser.LogicGreatLessExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicEqualsExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicEqualsExpr(JavaExprParser.LogicEqualsExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code leftIncDecAndSignExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeftIncDecAndSignExpr(JavaExprParser.LeftIncDecAndSignExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitAndExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitAndExpr(JavaExprParser.BitAndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link JavaExprParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExpr(JavaExprParser.AssignExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExprParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(JavaExprParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExprParser#primaryExprQualified}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExprQualified(JavaExprParser.PrimaryExprQualifiedContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExprParser#qualified}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified(JavaExprParser.QualifiedContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExprParser#primaryExprLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExprLiteral(JavaExprParser.PrimaryExprLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExprParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(JavaExprParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExprParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(JavaExprParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExprParser#qualifiedname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedname(JavaExprParser.QualifiednameContext ctx);
}