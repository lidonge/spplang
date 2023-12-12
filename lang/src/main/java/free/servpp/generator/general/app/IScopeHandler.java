package free.servpp.generator.general.app;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppCompilationUnit;
import free.servpp.generator.models.SppService;
import free.servpp.generator.models.app.AppHeader;
import free.servpp.generator.models.app.ScopeDefine;
import free.servpp.generator.models.app.ScopeItem;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.lang.antlr.AppParser;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public interface IScopeHandler extends IApplicationHandler {
    @Override
    default void enterScope(AppParser.ScopeContext ctx){
        RuleBlock currentRuleBlock = getCurrentRuleBlock();
        currentRuleBlock.setCurrentAnnotatable(currentRuleBlock.getAppScope());
    }

    @Override
    default void enterLocalBody(AppParser.LocalBodyContext ctx){
        getCurrentRuleBlock().getAppScope()._addLocalScope(new ScopeDefine());
    }
    @Override
    default void enterRemoteBody(AppParser.RemoteBodyContext ctx){
        getCurrentRuleBlock().getAppScope()._addRemoteScope(new ScopeDefine());
    }

    @Override
    default void enterInParameter(AppParser.InParameterContext ctx){
        String headerName = ctx.getChild(1).getText();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppHeader appHeader = ruleBlock.newOrExistAppHeader(headerName);
        ScopeDefine scopeDefine = getLastElement(ruleBlock.getAppScope().getScopelist());
        scopeDefine.setIn(appHeader);
    }

    @Override
    default void enterOutParameter(AppParser.OutParameterContext ctx){
        String headerName = ctx.getChild(1).getText();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppHeader appHeader = ruleBlock.newOrExistAppHeader(headerName);
        ScopeDefine scopeDefine = getLastElement(ruleBlock.getAppScope().getScopelist());
        scopeDefine.setOut(appHeader);
    }

    @Override
    default void enterScopeItem(AppParser.ScopeItemContext ctx){
        RuleBlock ruleBlock = getCurrentRuleBlock();
        ScopeDefine scopeDefine = getLastElement(ruleBlock.getAppScope().getScopelist());
        scopeDefine.addScopeItem(new ScopeItem());
    }

    @Override
    default void enterAsycnModifier(AppParser.AsycnModifierContext ctx){
        RuleBlock ruleBlock = getCurrentRuleBlock();
        ScopeDefine scopeDefine = getLastElement(ruleBlock.getAppScope().getScopelist());
        ScopeItem scopeItem = getLastElement(scopeDefine.getScopeItems());
        scopeItem.setAsync(true);
    }

    @Override
    default void enterTransModifier(AppParser.TransModifierContext ctx){
        TransactionType type = IConstance.TransactionType.valueOf(ctx.getChild(0).getText());
        RuleBlock ruleBlock = getCurrentRuleBlock();
        ScopeDefine scopeDefine = getLastElement(ruleBlock.getAppScope().getScopelist());
        ScopeItem scopeItem = getLastElement(scopeDefine.getScopeItems());
        scopeItem.setTransactionType(type);
    }

    @Override
    default void enterScopeItemIdentifier(AppParser.ScopeItemIdentifierContext ctx){
        String clsName = ctx.getChild(0).getText();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        ScopeDefine scopeDefine = getLastElement(ruleBlock.getAppScope().getScopelist());
        ScopeItem scopeItem = getLastElement(scopeDefine.getScopeItems());
        if("_default".equals(clsName)){
            for(SppCompilationUnit sppClass:getSppDomian().getMapsOfClass().values()){
                if(sppClass instanceof SppService && ((SppService) sppClass).getScopeItem() != null){
                    ScopeItem theItem = ((SppService) sppClass).getScopeItem();
                    if(theItem.isLocal() &&
                            theItem.getScopeDefine().getIn() == null &&
                            theItem.getScopeDefine().getOut() == null){//default entity services
                        scopeItem.setService((SppService) sppClass);
                    }
                }
            }
        }else {
            SppCompilationUnit sppClass = getSppDomian().getSppClass(NameUtil.firstToLowerCase(clsName, false));
            if (sppClass instanceof SppService) {
                scopeItem.setService((SppService) sppClass);
            } else {
                logSppError(ctx, clsName + " should be a service.");
            }
        }
    }
}
