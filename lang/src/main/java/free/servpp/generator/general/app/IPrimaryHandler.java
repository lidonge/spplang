package free.servpp.generator.general.app;

import free.servpp.generator.models.SppField;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.SppFieldDefine;
import free.servpp.lang.antlr.AppParser;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public interface IPrimaryHandler extends IApplicationHandler {

    @Override
    default void enterPrimaryQualified(AppParser.PrimaryQualifiedContext ctx){
        RuleBlock ruleBlock = getCurrentRuleBlock();
        String qualifiedName = ctx.getChild(0).getText();
        SppFieldDefine sppField = getQualifiedField(ctx, null, qualifiedName);
        if(sppField == null)
            logSppError(ctx, "File " + qualifiedName+" not defined");
        else
            ruleBlock.addPrimaryKey(qualifiedName,sppField);
    }
}
