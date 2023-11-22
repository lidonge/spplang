package free.servpp.generator.general.app;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.ISppErrorLogger;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppField;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.SppFieldDefine;
import free.servpp.lang.antlr.AppListener;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public interface IApplicationHandler extends AppListener, IConstance, ISppErrorLogger {

    RuleBlock getCurrentRuleBlock();

    SppDomain getSppDomian();

    default <T> T getLastElement(List<T> elements){
        return elements.get(elements.size()-1);
    }
    default <T> T removeLastElement(List<T> elements){
        return elements.remove(elements.size()-1);
    }
    default SppFieldDefine getQualifiedField(ParserRuleContext ctx, SppClass mapEntity, String qualifiedName) {
        SppField sppField = null;
        SppFieldDefine fieldDefine = null;
        String[] path = qualifiedName.split("\\.");
        if(path.length == 1){
            if(mapEntity == null){
                logSppError(ctx, qualifiedName + " is not a qualified name.");
            }else {
                sppField = mapEntity.getField(path[0]);
                fieldDefine = new SppFieldDefine(mapEntity,sppField);
            }
        }else {
            SppDomain sppDomian = getSppDomian();
            Iterator<String> iterator = Arrays.stream(path).iterator();
            SppClass sppClass = sppDomian.getSppClass(iterator.next());
            if (sppClass == null) {
                logSppError(ctx, "Entity " + path[0] + " not defined!");
            } else {
                while (iterator.hasNext()) {
                    sppField = sppClass.getField(iterator.next());
                    if (sppField == null) {
                        fieldDefine = null;
                        break;
                    }
                    fieldDefine = new SppFieldDefine(sppClass,sppField);
                    sppClass = sppField.getType();
                }
            }
        }
        return fieldDefine;
    }
}
