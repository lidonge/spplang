package free.servpp.generator.general.app;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppService;
import free.servpp.generator.models.app.AppHeader;
import free.servpp.generator.models.app.ScopeItem;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.ScopeBody;
import free.servpp.lang.antlr.AppParser;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public interface IScopeHandler extends IApplicationHandler {
    @Override
    default void enterScope(AppParser.ScopeContext ctx){
    }

    @Override
    default void enterLocalBody(AppParser.LocalBodyContext ctx){
        getCurrentRuleBlock().getAppScope()._addLocalScope(new ScopeBody());
    }
    @Override
    default void enterRemoteBody(AppParser.RemoteBodyContext ctx){
        getCurrentRuleBlock().getAppScope()._addRemoteScope(new ScopeBody());
    }

    @Override
    default void enterInParameter(AppParser.InParameterContext ctx){
        String headerName = ctx.getChild(0).getText();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppHeader appHeader = ruleBlock.newOrExistAppHeader(headerName);
        ScopeBody scopeBody = getLastElement(ruleBlock.getAppScope().getScopelist());
        scopeBody.setIn(appHeader);
    }

    @Override
    default void enterOutParameter(AppParser.OutParameterContext ctx){
        String headerName = ctx.getChild(0).getText();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppHeader appHeader = ruleBlock.newOrExistAppHeader(headerName);
        ScopeBody scopeBody = getLastElement(ruleBlock.getAppScope().getScopelist());
        scopeBody.setOut(appHeader);
    }

    @Override
    default void enterScopeItem(AppParser.ScopeItemContext ctx){
        RuleBlock ruleBlock = getCurrentRuleBlock();
        ScopeBody scopeBody = getLastElement(ruleBlock.getAppScope().getScopelist());
        scopeBody.addScopeItem(new ScopeItem());
    }

    @Override
    default void enterAsycnModifier(AppParser.AsycnModifierContext ctx){
        RuleBlock ruleBlock = getCurrentRuleBlock();
        ScopeBody scopeBody = getLastElement(ruleBlock.getAppScope().getScopelist());
        ScopeItem scopeItem = getLastElement(scopeBody.getScopeItems());
        scopeItem.setAsync(true);
    }

    @Override
    default void enterTransModifier(AppParser.TransModifierContext ctx){
        TransactionType type = IConstance.TransactionType.valueOf(ctx.getChild(0).getText());
        RuleBlock ruleBlock = getCurrentRuleBlock();
        ScopeBody scopeBody = getLastElement(ruleBlock.getAppScope().getScopelist());
        ScopeItem scopeItem = getLastElement(scopeBody.getScopeItems());
        scopeItem.setTransactionType(type);
    }

    @Override
    default void enterScopeItemIdentifier(AppParser.ScopeItemIdentifierContext ctx){
        String clsName = ctx.getChild(0).getText();
        SppClass sppClass = getSppDomian().getSppClass(NameUtil.firstToLowerCase(clsName,false));
        if(sppClass instanceof SppService){
            RuleBlock ruleBlock = getCurrentRuleBlock();
            ScopeBody scopeBody = getLastElement(ruleBlock.getAppScope().getScopelist());
            ScopeItem scopeItem = getLastElement(scopeBody.getScopeItems());
            scopeItem.setService((SppService) sppClass);
        }else{
            logSppError(ctx, clsName +" should be a service.");
        }
    }
}
