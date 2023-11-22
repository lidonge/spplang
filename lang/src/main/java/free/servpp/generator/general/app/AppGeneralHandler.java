package free.servpp.generator.general.app;

import free.servpp.generator.models.SppProject;
import free.servpp.lang.antlr.AppParser;
import free.servpp.lang.antlr.SppParser;

import java.io.File;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class AppGeneralHandler extends RuleBlockHandler {
    private SppProject sppProject;

    public AppGeneralHandler(File antlrFile, SppProject sppProject) {
        super(antlrFile);
        this.sppProject = sppProject;
    }

    @Override
    public void enterDomainName(AppParser.DomainNameContext ctx) {
        String domainName = ctx.getText();
        setSppDomain(sppProject.addDomain(domainName));
    }
}
