package free.servpp.generator.general.app;

import free.servpp.generator.models.IContainer;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.app.AppService;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.expr.IOperationExpression;

import java.io.File;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class RuleBlockHandler extends BaseHandler {
    File antlrFile;
    SppDomain sppDomain;
    IContainer currentContainer;
    @Override
    public IContainer getCurrentContainer(){
        return currentContainer;
    }

    @Override
    public void setCurrentContainer(IContainer container){
        this.currentContainer = container;
    }

    public RuleBlockHandler(File antlrFile) {
        this.antlrFile = antlrFile;
    }

    public SppDomain getSppDomain() {
        return sppDomain;
    }

    public void setSppDomain(SppDomain sppDomain) {
        this.sppDomain = sppDomain;
    }

    @Override
    public File getAntlrFile() {
        return antlrFile;
    }

    @Override
    public RuleBlock getCurrentRuleBlock() {
        return sppDomain.getRuleBlock();
    }

    @Override
    public SppDomain getSppDomian() {
        return sppDomain;
    }
}