package free.servpp.generator;

import free.servpp.generator.checker.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class ClassGenerator implements IFileGenerator {
    private File domainPath;
    private int braceLevel = 0;
    private SppClass sppClass;
    private String javaPackage;
    private PrintStream out;

    public ClassGenerator(File domainPath, SppClass sppClass, String javaPackage) {
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
                if (type == CompilationUnitType.role | type == CompilationUnitType.entity | type == CompilationUnitType.reference) {
                    printlnFromBeg(" implements " + typeToExtendString(type) + " {");
                    intoBrace();
                    genObject(type);
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
        printFromBeg("public void " + sppService.getFuncName() + "(");
        int count = 0;
        for (SppLocalVar field : sppLocalVars) {
            if (count != 0)
                print(",");
            print(field.getType().getName() + " " + field.getName());
            count++;
        }
    }

    private void genObject(CompilationUnitType type) {
        Map<String, SppField> sppFieldMap = sppClass.getSppFieldMap();
        for (SppField field : sppFieldMap.values()) {
            String sppType = field.getType().getName();
            if(field.getType().getType() == null)
                sppType = IFileGenerator.toJavaPrimaryType(sppType);
            printlnFromBeg("public " + sppType + " " + field.getName() + ";");
        }
    }


}
