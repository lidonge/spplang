package free.servpp.generator.general.app;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.ISppErrorLogger;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.app.IQualifiedNameUtil;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.SppFieldReference;
import free.servpp.lang.antlr.AppListener;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public interface IApplicationHandler extends AppListener, IConstance, ISppErrorLogger, IQualifiedNameUtil {

    void push(Object obj);
    Object pop();

    Object peek();
    RuleBlock getCurrentRuleBlock();

    SppDomain getSppDomian();

    default <T> T getLastElement(List<T> elements){
        return elements.get(elements.size()-1);
    }
    default <T> T removeLastElement(List<T> elements){
        return elements.remove(elements.size()-1);
    }
    default SppFieldReference getQualifiedField(ParserRuleContext ctx, SppClass mapEntity, String qualifiedName) {
        try {
            return getQualifiedField(getSppDomian(),mapEntity,qualifiedName);
        } catch (SemanticException e) {
            logSppError(ctx,e.getMessage());
        }
        return null;
    }
}
