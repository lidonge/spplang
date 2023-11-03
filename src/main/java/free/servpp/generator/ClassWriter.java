package free.servpp.generator;

import free.servpp.generator.models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class ClassWriter implements IFileGenerator {
    private File domainPath;
    private int braceLevel = 0;
    private SppClass sppClass;
    private String javaPackage;
    private PrintStream out;

    public ClassWriter(File domainPath, SppClass sppClass, String javaPackage) {
        this.domainPath = domainPath;
        this.sppClass = sppClass;
        this.javaPackage = javaPackage;
    }

    @Override
    public void setBraceLevel(int i) {
        braceLevel = i;
    }

    @Override
    public void incBraceLevel() {
        braceLevel++;
    }

    @Override
    public void decBraceLevel() {
        braceLevel--;
    }

    @Override
    public int getBraceLevel() {
        return braceLevel;
    }

    @Override
    public PrintStream getPrintStream() {
        return out;
    }

    @Override
    public File getDomainPath() {
        return domainPath;
    }

    public void generate() {
        String objName = sppClass.getName();
        File objPath = new File(getDomainPath(), objName + SUFFIX);
        try {
            CompilationUnitType type = sppClass.getType();
            if (type != null) {
                setBraceLevel(0);
                FileOutputStream fout = new FileOutputStream(objPath);
                out = new PrintStream(fout);
//            PrintStream out = System.out;
                println("package " + javaPackage + ";");
                print("public " + typeToString(sppClass.getType()) + " " + objName);
                if (type == CompilationUnitType.role | type == CompilationUnitType.entity |
                        type == CompilationUnitType.reference | type == CompilationUnitType.rolemapper) {
                    printlnFromBeg(" implements " + typeToExtendString(type) + " {");
                    intoBrace();
                    if(type != CompilationUnitType.rolemapper)
                        genObject(type);
                    else
                        genRoleMapper();
                } else if (type == CompilationUnitType.atomicservice | type == CompilationUnitType.scenario) {
                    printlnFromBeg((type == CompilationUnitType.atomicservice ? " extends " : " implements ") + typeToExtendString(type) + " {");
                    intoBrace();
                    if (type == CompilationUnitType.scenario) {
                        Map<String, SppField> sppFieldMap = sppClass.getSppFieldMap();
                        for (SppField var : sppFieldMap.values()) {
                            printlnFromBeg("public " + var.getType().getName() + " " + var.getName() + ";");
                        }
                    }
                    genServiceDefinition(type);
                    if (type == CompilationUnitType.atomicservice)
                        println(");");
                    else {
                        println("){");
                        intoBrace();
                        genServiceCalls();
                        outBrace();
                        printlnFromBeg("}");
                    }
                }
                out.println("}");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void genServiceCalls() {
        SppService sppService = (SppService) sppClass;
        List<IServiceBodyStatement> sppServiceCallList = sppService.getServiceBody().getSppServiceCallList();
        _genServiceCalls(sppServiceCallList);
    }

    private void _genServiceCalls(List<IServiceBodyStatement> sppServiceCallList) {
        for(IServiceBodyStatement sppServiceCall: sppServiceCallList) {
            if(sppServiceCall instanceof SppServiceCall) {
                genServiceCall((SppServiceCall) sppServiceCall);
            }
            else if(sppServiceCall instanceof TransactionBlock){
                genTransactionBlock((TransactionBlock) sppServiceCall);
            }
        }
    }

    private void genTransactionBlock(TransactionBlock sppServiceCall) {
        printlnFromBeg("{");
        intoBrace();
        printlnFromBeg("String transaction_id = startTransaction();");
        List<IServiceBodyStatement> sppServiceCallList = sppServiceCall.getSppServiceCallList();
        _genServiceCalls(sppServiceCallList);
        printlnFromBeg("endTransaction(transaction_id);");
        outBrace();
        printlnFromBeg("}");
    }

    private void genServiceCall(SppServiceCall sppServiceCall) {
        SppService callee = sppServiceCall.getCallee();
        if(callee != null) {
            printFromBeg(callee.getFuncName() + "." + callee.getFuncName() + "(");
            List<SppParameter> sppParameterList = sppServiceCall.getSppParameterList();
            int count = 0;
            for (SppParameter parameter : sppParameterList) {
                if (count != 0)
                    print(",");
                print(parameter.getName());
                count++;
            }
            println(");");
        }
    }

    private void genServiceDefinition(CompilationUnitType type) {
        SppService sppService = (SppService) sppClass;
        List< ? extends SppLocalVar> sppLocalVars = type == CompilationUnitType.atomicservice ? sppService.getSppFieldList()
                : sppService.getServiceBody().getSppLocalVarList();
        String retType = "void";
        if(sppService.getServiceType() == ServiceType.check){
            retType = "boolean";
        }
        printFromBeg("public "+retType+ " " + sppService.getFuncName() + "(");
        int count = 0;
        for (SppLocalVar field : sppLocalVars) {
            if (count != 0)
                print(",");
            print(field.getType().getName() + " " + field.getName());
            count++;
        }
    }

    private void genObject(CompilationUnitType type) {
        List<SppField> sppFieldList = sppClass.getSppFieldList();
        for (SppField field : sppFieldList) {
            String sppType = field.getType().getName();
            if(field.getType().getType() == null)
                sppType = IFileGenerator.toJavaPrimaryType(sppType);
            printlnFromBeg("public " + sppType + " " + field.getName() + ";");
        }
    }

    private void genRoleMapper() {
        SppRoleMapper sppRoleMapper = (SppRoleMapper) sppClass;
        SppClass entity = sppRoleMapper.getEntity();
        SppClass role = sppRoleMapper.getRole();
        Map<String,SppLocalVar> nameToEntitys = new HashMap<>();
        for(SppLocalVar var: entity.getSppFieldList()){
            nameToEntitys.put(var.getName(),var);
        }
//        roleToEntity(role, entity, sppRoleMapper, nameToEntitys);
        aToB(role, entity, sppRoleMapper, nameToEntitys);
        aToB(entity,role,sppRoleMapper,nameToEntitys);
    }

    private void aToB(SppClass a, SppClass b, SppRoleMapper sppRoleMapper, Map<String, SppLocalVar> nameToEntitys) {
        genMapFunctionDef(a, b);
        intoBrace();
        SppClass role = a.getType() == CompilationUnitType.role ? a :b;
        SppClass entity = a.getType() == CompilationUnitType.role ? b :a;
        String aname1 = a.getType() == CompilationUnitType.role ? "role." :"entity.";
        String bname1 = a.getType() == CompilationUnitType.role ? "entity." :"role.";
        genMapSame(sppRoleMapper, nameToEntitys, role, entity, bname1, aname1);
        genMapWithDef(a,sppRoleMapper, bname1, aname1);
        outBrace();
        printlnFromBeg("}");
    }

    private void genMapWithDef(SppClass a, SppRoleMapper sppRoleMapper, String bname1, String aname1) {
        List<SppField> sppRoleFieldList= sppRoleMapper.getSppFieldList();
        if(sppRoleFieldList != null){
            for (SppField sppField: sppRoleFieldList){
                SppRoleField field = (SppRoleField) sppField;
                String aname = a.getType() == CompilationUnitType.role ? field.getName() : field.getEntityName();
                String bname = a.getType() == CompilationUnitType.role ? field.getEntityName() : field.getName();

                printlnFromBeg(bname1 +bname +" = " + aname1 +aname+";");
            }
        }
    }

    private void genMapSame(SppRoleMapper sppRoleMapper, Map<String, SppLocalVar> nameToEntitys, SppClass role, SppClass entity, String bname1, String aname1) {
        printlnFromBeg(role.getName()+" role = ("+ role.getName()+")r;");
        printlnFromBeg(entity.getName()+" entity = ("+ entity.getName()+")e;");
        if(sppRoleMapper.isMapSame()){
            for(SppLocalVar var: role.getSppFieldList()){
                SppLocalVar entVar = nameToEntitys.get(var.getName());
                if(entVar != null){
                    printlnFromBeg(bname1 +entVar.getName() +" = " + aname1 +var.getName() +";");
                }
            }
        }
    }

    private void genMapFunctionDef(SppClass a, SppClass b) {
        String funcName = a.getType() == CompilationUnitType.role ? "roleToEntity" :"entityToRole";
        String aname = a.getType() == CompilationUnitType.role ? "r" :"e";
        String bname = a.getType() == CompilationUnitType.role ? "e" :"r";
        printlnFromBeg("public void "+funcName+"("+
                typeToExtendString(a.getType())+" "+aname+", "+
                typeToExtendString(b.getType())+" "+bname+") {");
    }

}
