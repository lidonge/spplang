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
        String[] sdims = type.split("\\[");

        SppDomain sppDomain = checkClass(ctx, sdims[0]);
        CompilationUnitType clstype = sppDomain.getCurrentClass().getType();
        SppCompilationUnit sppType = sppDomain.getSppClass(clstype, sdims[0]);
        int dim = sdims.length -1;
//        if( (clstype == CompilationUnitType.entity || clstype == CompilationUnitType.reference) &&
//                (sppType.getType() != null && sppType.getType() != CompilationUnitType.Enum) )
//            logSppError(ctx, "The field "+name+" of entity should be primary type!" );
        logSppError(ctx,generateField(sdims[0],dim,name));
    }
}
