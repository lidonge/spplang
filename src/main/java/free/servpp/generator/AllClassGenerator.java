package free.servpp.generator;

import free.servpp.generator.models.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class AllClassGenerator {
    public void generate(ClassChecker classChecker, File domainPath, String javaPackege){
        dealMaps(classChecker);
        Map<String, SppClass> sppClassMap = classChecker.getMapsOfClass();
        for(SppClass sppClass: sppClassMap.values()){
//            if(sppClass.getType() == IConstance.CompilationUnitType.rolemapper)
//                System.out.println(sppClass);
            new ClassWriter(domainPath,sppClass,javaPackege).generate();
        }
    }
    public void dealMaps(ClassChecker classChecker){
        Map<String, SppClass> sppClassMap = classChecker.getMapsOfClass();
        for(SppClass sppClass: sppClassMap.values()){
            if(sppClass.getType() == IConstance.CompilationUnitType.rolemapper) {
//                System.out.println(sppClass);
                Map<String,String> entityToRole = new HashMap<>();
                SppRoleMapper sppRoleMapper = (SppRoleMapper) sppClass;
                for(SppLocalVar var:sppRoleMapper.getSppFieldList()){
                    SppRoleField roleField = (SppRoleField) var;
                    entityToRole.put(roleField.getEntityName(), roleField.getName());
                }
                takeAll(sppRoleMapper, entityToRole);
            }
        }
    }

    private void takeAll(SppRoleMapper sppRoleMapper, Map<String, String> entityToRole) {
        if(sppRoleMapper.isTakeAll()){
            SppClass entity = sppRoleMapper.getEntity();
            SppClass role = sppRoleMapper.getRole();
            for (SppLocalVar var : entity.getSppFieldList()){
                if(entityToRole.get(var.getName()) == null)
                    role.addField((SppField) var);
            }
        }
    }
}
