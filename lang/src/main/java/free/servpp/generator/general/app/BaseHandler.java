package free.servpp.generator.general.app;

import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.lang.antlr.AppParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;


/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public abstract class BaseHandler implements IHeaderHandler, IPrimaryHandler,
        IMapperHandler, IScopeHandler, IServicesHandler, IAnnotateHandler {

    @Override
    public void exitMapParameterIdentifier(AppParser.MapParameterIdentifierContext ctx) {

    }

    @Override
    public void enterQualifiedName(AppParser.QualifiedNameContext ctx) {

    }

    @Override
    public void exitQualifiedName(AppParser.QualifiedNameContext ctx) {

    }

    @Override
    public void exitRemoteBody(AppParser.RemoteBodyContext ctx) {

    }

    @Override
    public void enterDefaultValue(AppParser.DefaultValueContext ctx) {

    }

    @Override
    public void exitDefaultValue(AppParser.DefaultValueContext ctx) {

    }

    @Override
    public void exitTransModifier(AppParser.TransModifierContext ctx) {

    }

    @Override
    public void exitPrecision(AppParser.PrecisionContext ctx) {

    }

    @Override
    public void exitHeaderFieldDefine(AppParser.HeaderFieldDefineContext ctx) {

    }

    @Override
    public void enterProgram(AppParser.ProgramContext ctx) {

    }

    @Override
    public void exitProgram(AppParser.ProgramContext ctx) {

    }

    @Override
    public void exitLocalBody(AppParser.LocalBodyContext ctx) {

    }

    @Override
    public void exitHeaderFieldDefineIdentifier(AppParser.HeaderFieldDefineIdentifierContext ctx) {

    }

    @Override
    public void exitPrimaryQualified(AppParser.PrimaryQualifiedContext ctx) {

    }

    @Override
    public void exitExtendsHeaders(AppParser.ExtendsHeadersContext ctx) {

    }

    @Override
    public void exitSqlTypePrimitiveType(AppParser.SqlTypePrimitiveTypeContext ctx) {

    }

    @Override
    public void exitAsycnModifier(AppParser.AsycnModifierContext ctx) {

    }

    @Override
    public void enterServiceDomain(AppParser.ServiceDomainContext ctx) {

    }

    @Override
    public void exitServiceDomain(AppParser.ServiceDomainContext ctx) {

    }

    @Override
    public void enterDomainName(AppParser.DomainNameContext ctx) {

    }

    @Override
    public void exitDomainName(AppParser.DomainNameContext ctx) {

    }

    @Override
    public void enterMapParameters(AppParser.MapParametersContext ctx) {

    }

    @Override
    public void exitMapParameters(AppParser.MapParametersContext ctx) {

    }

    @Override
    public void exitNotnull(AppParser.NotnullContext ctx) {

    }

    @Override
    public void exitHeader(AppParser.HeaderContext ctx) {

    }

    @Override
    public void exitHeaderFieldModifierNotNull(AppParser.HeaderFieldModifierNotNullContext ctx) {

    }

    @Override
    public void enterPrimary(AppParser.PrimaryContext ctx) {

    }

    @Override
    public void exitPrimary(AppParser.PrimaryContext ctx) {

    }

    @Override
    public void exitHeaderFieldModifierUnique(AppParser.HeaderFieldModifierUniqueContext ctx) {

    }

    @Override
    public void exitOutParameter(AppParser.OutParameterContext ctx) {

    }

    @Override
    public void enterRuleBlock(AppParser.RuleBlockContext ctx) {

    }

    @Override
    public void exitRuleBlock(AppParser.RuleBlockContext ctx) {

    }

    @Override
    public void exitInParameter(AppParser.InParameterContext ctx) {

    }

    @Override
    public void exitScale(AppParser.ScaleContext ctx) {

    }

    @Override
    public void enterLiteral(AppParser.LiteralContext ctx) {

    }

    @Override
    public void exitLiteral(AppParser.LiteralContext ctx) {

    }

    @Override
    public void enterSqlTypeLength(AppParser.SqlTypeLengthContext ctx) {

    }

    @Override
    public void exitSqlTypeLength(AppParser.SqlTypeLengthContext ctx) {

    }

    @Override
    public void enterPrimaryExpr(AppParser.PrimaryExprContext ctx) {

    }

    @Override
    public void exitPrimaryExpr(AppParser.PrimaryExprContext ctx) {

    }

    @Override
    public void exitMapItem(AppParser.MapItemContext ctx) {

    }

    @Override
    public void exitScope(AppParser.ScopeContext ctx) {

    }

    @Override
    public void exitOverride(AppParser.OverrideContext ctx) {

    }

    @Override
    public void exitMap(AppParser.MapContext ctx) {

    }

    @Override
    public void enterPrimitiveType(AppParser.PrimitiveTypeContext ctx) {

    }

    @Override
    public void exitPrimitiveType(AppParser.PrimitiveTypeContext ctx) {

    }

    @Override
    public void enterScopeDefine(AppParser.ScopeDefineContext ctx) {

    }

    @Override
    public void exitScopeDefine(AppParser.ScopeDefineContext ctx) {

    }

    @Override
    public void enterPrimExpr(AppParser.PrimExprContext ctx) {

    }

    @Override
    public void exitPrimExpr(AppParser.PrimExprContext ctx) {

    }

    @Override
    public void enterTransactionModifier(AppParser.TransactionModifierContext ctx) {

    }

    @Override
    public void exitTransactionModifier(AppParser.TransactionModifierContext ctx) {

    }

    @Override
    public void exitDefaultValueliteral(AppParser.DefaultValueliteralContext ctx) {

    }

    @Override
    public void enterServices(AppParser.ServicesContext ctx) {

    }

    @Override
    public void exitServices(AppParser.ServicesContext ctx) {

    }

    @Override
    public void enterServiceBody(AppParser.ServiceBodyContext ctx) {

    }

    @Override
    public void exitServiceBody(AppParser.ServiceBodyContext ctx) {

    }

    @Override
    public void exitScopeItem(AppParser.ScopeItemContext ctx) {

    }

    @Override
    public void enterSqlType(AppParser.SqlTypeContext ctx) {

    }

    @Override
    public void exitSqlType(AppParser.SqlTypeContext ctx) {

    }

    @Override
    public void exitService(AppParser.ServiceContext ctx) {

    }

    @Override
    public void enterScopeParameter(AppParser.ScopeParameterContext ctx) {

    }

    @Override
    public void exitScopeParameter(AppParser.ScopeParameterContext ctx) {

    }

    @Override
    public void exitScopeItemIdentifier(AppParser.ScopeItemIdentifierContext ctx) {

    }

    @Override
    public void exitHeaderFieldDefinePrimitiveType(AppParser.HeaderFieldDefinePrimitiveTypeContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
