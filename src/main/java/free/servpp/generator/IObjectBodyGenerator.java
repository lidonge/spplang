package free.servpp.generator;

import free.servpp.SppParser;
import free.servpp.generator.checker.*;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IObjectBodyGenerator extends ICompilationUnitGenerator{
    @Override
    default void enterObjectBody(SppParser.ObjectBodyContext ctx) {
    }

    @Override
    default void exitObjectBody(SppParser.ObjectBodyContext ctx) {
    }

    @Override
    default void enterFieldDeclaration(SppParser.FieldDeclarationContext ctx) {
        String type = ctx.getChild(0).getText();
        String name = ctx.getChild(1).getText();
        ClassChecker classChecker = checkClass(ctx, type);
        ServiceBody bodyChecker = classChecker.getCurrentClass().getServiceBody();
        SppClass cls = bodyChecker.getRealm();
        String err = generateField(type,name);
        logSppError(ctx,err);
    }
}
