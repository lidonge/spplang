package free.servpp.generator.general.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppCompilationUnit;
import free.servpp.generator.models.app.AppHeader;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.SppExtendField;
import free.servpp.lang.antlr.AppParser;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public interface IHeaderHandler extends IApplicationHandler {
    @Override
    default void enterHeader(@NotNull AppParser.HeaderContext ctx){
        AppHeader appHeader = new AppHeader();
        String name = ctx.getChild(1).getText();
        appHeader.setName(name);
        String err = getCurrentRuleBlock().addAppHeader(appHeader);
        getCurrentRuleBlock().setCurrentAnnotatable(appHeader);
        if(err != null){
            logSppError(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(),err);
        }
    }

    @Override
    default void enterExtendsHeaders(@NotNull AppParser.ExtendsHeadersContext ctx){
        int count = ctx.getChildCount();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppHeader curHeader = getLastElement(ruleBlock.getHeaders());;
        for(int i = 0;i<count;i++){
            AppHeader header = ruleBlock.newOrExistAppHeader(ctx.getChild(i).getText());
            curHeader.addExtendsHeader(header);
        }
    }

    @Override
    default void enterHeaderFieldDefine(AppParser.HeaderFieldDefineContext ctx){
        AppHeader currentHeader = getLastElement(getCurrentRuleBlock().getHeaders());

        SppExtendField field = new SppExtendField(null,null);
        currentHeader._addExtendField(field);
    }

    @Override
    default void enterHeaderFieldModifierNotNull(AppParser.HeaderFieldModifierNotNullContext ctx){
        AppHeader appHeader = getLastElement(getCurrentRuleBlock().getHeaders());
        SppExtendField sppExtendField = getLastElement(appHeader.getSppExtendFields());
        sppExtendField.setNotNull(true);
    }

    @Override
    default void enterHeaderFieldModifierUnique(AppParser.HeaderFieldModifierUniqueContext ctx){
        AppHeader appHeader = getLastElement(getCurrentRuleBlock().getHeaders());
        SppExtendField sppExtendField = getLastElement(appHeader.getSppExtendFields());
        sppExtendField.setUnique(true);
    }

    @Override
    default void enterHeaderFieldDefinePrimitiveType(AppParser.HeaderFieldDefinePrimitiveTypeContext ctx){
        String text = ctx.getChild(0).getText();
        SppCompilationUnit type = getSppDomian().getSppClass(null,text);
        AppHeader appHeader = getLastElement(getCurrentRuleBlock().getHeaders());
        SppExtendField sppExtendField = getLastElement(appHeader.getSppExtendFields());
        sppExtendField.setType(type);
    }

    @Override
    default void enterHeaderFieldDefineIdentifier(AppParser.HeaderFieldDefineIdentifierContext ctx){
        AppHeader appHeader = getLastElement(getCurrentRuleBlock().getHeaders());
        SppExtendField sppExtendField = getLastElement(appHeader.getSppExtendFields());
        removeLastElement(appHeader.getSppExtendFields());
        sppExtendField.setName(ctx.getChild(0).getText());
        String err = appHeader.addExtendField(sppExtendField);
        if(err != null)
            logSppError(ctx,err);
    }
    @Override
    default void enterDefaultValueliteral(AppParser.DefaultValueliteralContext ctx){
        AppHeader appHeader = getLastElement(getCurrentRuleBlock().getHeaders());
        SppExtendField sppExtendField = getLastElement(appHeader.getSppExtendFields());
        sppExtendField.setDefaultValue(ctx.getChild(0).getText());
    }

    @Override
    default void enterOverride(AppParser.OverrideContext ctx){
        AppHeader appHeader = getLastElement(getCurrentRuleBlock().getHeaders());
        SppExtendField sppExtendField = getLastElement(appHeader.getSppExtendFields());
        sppExtendField.setOverrideSuper(ctx.getChild(0).getText());
    }
}
