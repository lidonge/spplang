package free.servpp.generator.models.app;

import free.servpp.generator.general.ISppErrorLogger;
import free.servpp.generator.general.app.SemanticException;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.SppField;
import free.servpp.generator.models.SppLocalVar;
import free.servpp.generator.models.app.SppFieldDefine;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public interface IQualifiedNameUtil {
    default SppClass getQualifieField(String qualifiedName, Map<String, ? extends SppLocalVar> sppLocalVarHashMap){
        String[] parts = qualifiedName.split("\\.");
        SppLocalVar inst = sppLocalVarHashMap.get(parts[0]);
        if(inst == null)
            return null;
        SppClass cls = inst.getType();
        for(int i = 1;i<parts.length;i++){
            SppField field = cls.getField(parts[i]);
            if(field == null){
                return null;
            }
            cls = field.getType();
        }
        return cls;

    }

    default SppFieldDefine getQualifiedField(SppDomain sppDomain, SppClass mapEntity, String qualifiedName) throws SemanticException {
        SppField sppField = null;
        SppFieldDefine fieldDefine = null;
        String[] path = qualifiedName.split("\\.");
        if(path.length == 1){
            if(mapEntity == null){
                throw new SemanticException(qualifiedName + " is not a qualified name.");
            }else {
                sppField = mapEntity.getField(path[0]);
                fieldDefine = new SppFieldDefine(mapEntity,sppField);
            }
        }else {
            Iterator<String> iterator = Arrays.stream(path).iterator();
            SppClass sppClass = sppDomain.getSppClass(iterator.next());
            if (sppClass == null) {
                throw new SemanticException("Entity " + path[0] + " not defined!");
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
