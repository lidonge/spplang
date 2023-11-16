package free.servpp.generator.java;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.models.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public interface IRoleMapperWriter extends IClassWriter {
//    public RoleMapperClassWriter(File domainPath, SppClass sppClass, String javaPackage) {
//        super(domainPath, sppClass, javaPackage);
//    }

    default void genRoleMapper(SppRoleMapper sppRoleMapper) throws FileNotFoundException {
        genClassDefine(sppRoleMapper , "model");
//        SppRoleMapper sppRoleMapper = (SppRoleMapper) sppClass;
        SppClass entity = sppRoleMapper.getEntity();
        SppClass role = sppRoleMapper.getRole();
        Map<String, SppLocalVar> nameToEntitys = new HashMap<>();
        for (SppLocalVar var : entity.getSppFieldList()) {
            nameToEntitys.put(var.getName(), var);
        }
//        roleToEntity(role, entity, sppRoleMapper, nameToEntitys);
        aToB(role, entity, sppRoleMapper, nameToEntitys);
        aToB(entity, role, sppRoleMapper, nameToEntitys);
        println("}");
    }

    private void aToB(SppClass a, SppClass b, SppRoleMapper sppRoleMapper, Map<String, SppLocalVar> nameToEntitys) {
        genMapFunctionDef(a, b);
        intoBrace();
        SppClass role = a.getType() == IConstance.CompilationUnitType.role ? a : b;
        SppClass entity = a.getType() == IConstance.CompilationUnitType.role ? b : a;
        String aname1 = a.getType() == IConstance.CompilationUnitType.role ? "role." : "entity.";
        String bname1 = a.getType() == IConstance.CompilationUnitType.role ? "entity." : "role.";
        genMapSame(sppRoleMapper, nameToEntitys, role, entity, bname1, aname1);
        genMapWithDef(a, sppRoleMapper, bname1, aname1);
        outBrace();
        printlnFromBeg("}");
    }

    private void genMapWithDef(SppClass a, SppRoleMapper sppRoleMapper, String bname1, String aname1) {
        List<SppField> sppRoleFieldList = sppRoleMapper.getSppFieldList();
        if (sppRoleFieldList != null) {
            for (SppField sppField : sppRoleFieldList) {
                SppRoleField field = (SppRoleField) sppField;
                String aname = a.getType() == IConstance.CompilationUnitType.role ? field.getName() : field.getEntityName();
                String bname = a.getType() == IConstance.CompilationUnitType.role ? field.getEntityName() : field.getName();

//                printlnFromBeg(bname1 + bname + " = " + aname1 + aname + ";");
                printlnFromBeg(genMapField(bname1,aname1, aname, bname) );
            }
        }
    }

    private void genMapSame(SppRoleMapper sppRoleMapper, Map<String, SppLocalVar> nameToEntitys, SppClass role, SppClass entity, String bname1, String aname1) {
//        printlnFromBeg(role.getName() + " role = (" + role.getName() + ")r;");
//        printlnFromBeg(entity.getName() + " entity = (" + entity.getName() + ")e;");
        if (sppRoleMapper.isMapSame()) {
            for (SppLocalVar var : role.getSppFieldList()) {
                SppLocalVar entVar = nameToEntitys.get(var.getName());
                if (entVar != null) {
                    printlnFromBeg(genMapField(bname1, aname1, var.getName(), entVar.getName()));
                }
            }
        }
    }

    private String genMapField(String bname1, String aname1, String  aname2, String bname2) {
        return bname1 + "set" + IConstance.firstToLowerCase(bname2, false) + " (" +
                aname1 + "get" + IConstance.firstToLowerCase(aname2, false) + "());";
    }

    private void genMapFunctionDef(SppClass a, SppClass b) {
        String funcName = a.getType() == IConstance.CompilationUnitType.role ? "roleToEntity" : "entityToRole";
        String aname = a.getType() == IConstance.CompilationUnitType.role ? "role" : "entity";
        String bname = a.getType() == IConstance.CompilationUnitType.role ? "entity" : "role";
        printlnFromBeg("public void " + funcName + "(" +
                a.getName() + " " + aname + ", " +
                b.getName() + " " + bname + ") {");
    }
}
