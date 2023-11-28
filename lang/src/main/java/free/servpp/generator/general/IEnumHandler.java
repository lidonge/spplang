package free.servpp.generator.general;

import free.servpp.generator.models.SppEnum;
import free.servpp.lang.antlr.SppParser;

/**
 * @author lidong@date 2023-11-28@version 1.0
 */
public interface IEnumHandler extends ICompilationUnitHandler{
    @Override
    default void enterEnumeration(SppParser.EnumerationContext ctx){
        String name = ctx.getChild(1).getText();
        if(getSppDomain().getSppClass(name)!= null){
            logSppError(ctx, "Duplicate enum " + name);
        }
        getSppDomain().addClass(new SppEnum(name));
    }

    @Override
    default void exitEnumeration(SppParser.EnumerationContext ctx){

    }

    @Override
    default void enterEnumBody(SppParser.EnumBodyContext ctx){
    }

    @Override
    default void exitEnumBody(SppParser.EnumBodyContext ctx){

    }

    @Override
    default void enterEnumBodyIdentifier(SppParser.EnumBodyIdentifierContext ctx){
        SppEnum sppEnum = (SppEnum) getSppDomain().getCurrentClass();
        sppEnum.getItems().add(ctx.getChild(0).getText());
    }

    @Override
    default void exitEnumBodyIdentifier(SppParser.EnumBodyIdentifierContext ctx){

    }
}
