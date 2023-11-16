package free.servpp.generator.java;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.models.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public interface IScenarioWriter extends IServiceWriter{
    default void genScenario( SppClass sppClass) throws FileNotFoundException {
        IConstance.CompilationUnitType type = sppClass.getType();
        genClassDefine(sppClass);
        genServiceDefinition(type, (SppService) sppClass);
        println("{");
        intoBrace();
        genServiceCalls((SppService) sppClass);
        outBrace();
        printlnFromBeg("}");
        if (type == IConstance.CompilationUnitType.scenario) {
            Map<String, SppField> sppFieldMap = sppClass.getSppFieldMap();
            for (SppField var : sppFieldMap.values()) {
                printlnFromBeg("public " + var.getType().getName() + " " + var.getName() + ";");
            }
        }
        println("}");
    }
    private void genServiceCalls(SppService sppService) {
//        SppService sppService = (SppService) sppClass;
        List<IServiceBodyStatement> sppServiceCallList = sppService.getServiceBody().getSppServiceCallList();
        _genServiceCalls(sppServiceCallList);
    }

    private void _genServiceCalls(List<IServiceBodyStatement> sppServiceCallList) {
        for (IServiceBodyStatement sppServiceCall : sppServiceCallList) {
            if (sppServiceCall instanceof SppServiceCall) {
                genServiceCall((SppServiceCall) sppServiceCall);
            } else if (sppServiceCall instanceof TransactionBlock) {
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
        if (callee != null) {
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

}
