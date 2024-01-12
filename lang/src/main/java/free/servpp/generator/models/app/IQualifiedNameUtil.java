package free.servpp.generator.models.app;

import free.servpp.generator.general.app.SemanticException;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.SppField;
import free.servpp.generator.models.SppLocalVar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public interface IQualifiedNameUtil {
    default SppLocalVar getQualifieField(String qualifiedName, Map<String, ? extends SppLocalVar> sppLocalVarHashMap){
        String[] parts = qualifiedName.split("\\.");
        SppLocalVar inst = sppLocalVarHashMap.get(parts[0]);
        if(inst == null)
            return null;
        SppClass cls = (SppClass) inst.getType();
        for(int i = 1;i<parts.length;i++){
            inst = cls.getField(parts[i]);

            if(inst == null){
                return null;
            }
            cls = (SppClass) inst.getType();
        }
        return inst;

    }

    default SppFieldReference getQualifiedField(SppDomain sppDomain, SppClass mapEntity, String qualifiedName) throws SemanticException {
        SppField sppField = null;
        SppFieldReference fieldDefine = null;
        String[] path = qualifiedName.split("\\.");
        if(path.length == 1){
            if(mapEntity == null){
                throw new SemanticException(qualifiedName + " is not a qualified name.");
            }else {
                sppField = mapEntity.getField(path[0]);
                fieldDefine = new SppFieldReference(sppField);
            }
        }else {
            Iterator<String> iterator = Arrays.stream(path).iterator();
            SppClass sppClass = (SppClass) sppDomain.getSppClass(iterator.next());
            if (sppClass == null) {
                throw new SemanticException("Entity " + path[0] + " not defined!");
            } else {
                while (iterator.hasNext()) {
                    sppField = sppClass.getField(iterator.next());
                    if (sppField == null) {
                        fieldDefine = null;
                        break;
                    }
                    fieldDefine = new SppFieldReference(sppField);
                    sppClass = (SppClass) sppField.getType();
                }
            }
        }
        fieldDefine = sppDomain.addSppFieldReference(fieldDefine.getSppField());
        return fieldDefine;
    }

}
