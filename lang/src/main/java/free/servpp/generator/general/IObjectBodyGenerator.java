package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppParser;

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

        if(classChecker.getCurrentClass().getType() == CompilationUnitType.entity &&
                classChecker.getSppClass(classChecker.getCurrentClass().getType(),type).getType() != null)
            logSppError(ctx, "The field "+name+" of entity should be primary type!" );
        logSppError(ctx,generateField(type,name));
    }
}
