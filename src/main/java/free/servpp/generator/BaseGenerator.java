package free.servpp.generator;

import free.servpp.SppParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public abstract class BaseGenerator implements
        IEntityGenerator, IRoleGenerator, IAtomicServiceGenerator, IScenarioGenerator, IActasGenerator{

    @Override
    public void enterProgram(SppParser.ProgramContext ctx) {

    }

    @Override
    public void exitProgram(SppParser.ProgramContext ctx) {

    }

    @Override
    public void enterServicedomain(SppParser.ServicedomainContext ctx) {

    }

    @Override
    public void exitServicedomain(SppParser.ServicedomainContext ctx) {

    }

    @Override
    public void enterDomainname(SppParser.DomainnameContext ctx) {

    }

    @Override
    public void exitDomainname(SppParser.DomainnameContext ctx) {

    }

    @Override
    public void enterCompilationUnits(SppParser.CompilationUnitsContext ctx) {

    }

    @Override
    public void exitCompilationUnits(SppParser.CompilationUnitsContext ctx) {

    }

    @Override
    public void enterCompilationUnit(SppParser.CompilationUnitContext ctx) {

    }

    @Override
    public void exitCompilationUnit(SppParser.CompilationUnitContext ctx) {

    }

    @Override
    public void exitObject(SppParser.ObjectContext ctx) {

    }

    @Override
    public void enterObjectBodyDeclaration(SppParser.ObjectBodyDeclarationContext ctx) {

    }

    @Override
    public void exitObjectBodyDeclaration(SppParser.ObjectBodyDeclarationContext ctx) {

    }

    @Override
    public void exitFieldDeclaration(SppParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void enterVariableDeclaratorId(SppParser.VariableDeclaratorIdContext ctx) {

    }

    @Override
    public void exitVariableDeclaratorId(SppParser.VariableDeclaratorIdContext ctx) {

    }

    @Override
    public void enterType(SppParser.TypeContext ctx) {

    }

    @Override
    public void exitType(SppParser.TypeContext ctx) {

    }

    @Override
    public void exitRole(SppParser.RoleContext ctx) {

    }

    @Override
    public void enterRoleBody(SppParser.RoleBodyContext ctx) {

    }

    @Override
    public void exitRoleBody(SppParser.RoleBodyContext ctx) {

    }


    @Override
    public void enterObjecttype(SppParser.ObjecttypeContext ctx) {

    }

    @Override
    public void exitObjecttype(SppParser.ObjecttypeContext ctx) {

    }

    @Override
    public void exitParameterDeclaration(SppParser.ParameterDeclarationContext ctx) {

    }

    @Override
    public void enterRoletype(SppParser.RoletypeContext ctx) {

    }

    @Override
    public void exitRoletype(SppParser.RoletypeContext ctx) {

    }

    @Override
    public void enterScenarioDeclaration(SppParser.ScenarioDeclarationContext ctx) {

    }

    @Override
    public void enterQualifiedname(SppParser.QualifiednameContext ctx) {

    }

    @Override
    public void exitQualifiedname(SppParser.QualifiednameContext ctx) {

    }

    @Override
    public void enterPrimitiveType(SppParser.PrimitiveTypeContext ctx) {

    }

    @Override
    public void exitPrimitiveType(SppParser.PrimitiveTypeContext ctx) {

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
