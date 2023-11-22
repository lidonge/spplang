package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppParser;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IEntityBodyHandler extends ICompilationUnitHandler {
    @Override
    default void enterEntityBody(SppParser.EntityBodyContext ctx) {
    }

    @Override
    default void exitEntityBody(SppParser.EntityBodyContext ctx) {
    }

    @Override
    default void enterFieldDeclaration(SppParser.FieldDeclarationContext ctx) {
        String type = ctx.getChild(0).getText();
        String name = ctx.getChild(1).getText();
        SppDomain sppDomain = checkClass(ctx, type);
        CompilationUnitType clstype = sppDomain.getCurrentClass().getType();
        if( (clstype == CompilationUnitType.entity || clstype == CompilationUnitType.reference) &&
                sppDomain.getSppClass(clstype,type).getType() != null)
            logSppError(ctx, "The field "+name+" of entity should be primary type!" );
        logSppError(ctx,generateField(type,name));
    }
}
