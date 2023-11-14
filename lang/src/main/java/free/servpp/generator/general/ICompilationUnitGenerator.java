package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppListener;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface ICompilationUnitGenerator extends SppListener, IConstance {
    File getDomainPath();

    File getSppFile();

    ClassChecker getClassChecker();

    void push(Object obj);
    Object pop();
    Object peek();

    int stackSize();
//    Stack getStack();
    default String generateLocalVar(String ptype, String name) {
        SppVarMaker<SppClass, String> maker = (cls, s1) ->{return new SppLocalVar(cls,s1);};
        return _generateField(ptype,name,true,maker);
    }
    default String generateField(String ptype, String name) {
        SppVarMaker<SppClass, String> maker = (cls, s1) ->{return new SppField(cls,s1);};
        return  _generateField(ptype,name,false,maker);
    }
    default String generateField(String ptype, String name, SppVarMaker<SppClass, String> maker) {
        return  _generateField(ptype,name,false,maker);
    }
    private String _generateField(String ptype, String name, boolean bLocal,SppVarMaker<SppClass, String> maker) {
        ClassChecker checker = getClassChecker();
        SppClass typeClass = checker.getSppClass(CompilationUnitType.entity, ptype);
        SppLocalVar var = maker.create(typeClass,name);
        if(bLocal)
            return checker.getCurrentClass().addLocalVar(var);
        else
            return checker.getCurrentClass().addField((SppField) var);
    }

    default SppService generateService(CompilationUnitType type, ParserRuleContext ctx, String objName, String methodName) {
        SppService ret = (SppService) generateClass(type,ctx,objName);
        ret.setFuncName(methodName);
        return ret;
    }
    default SppClass generateClass(CompilationUnitType type, ParserRuleContext ctx, String objName) {
        ClassChecker glbChecker = getClassChecker();
        String err = glbChecker.checkDupClass(objName);
        if(err != null){
            logSppError(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(),err);
        }
        SppClass cls = genObjectDecl(type, objName);
        cls.setType(type);
        cls = glbChecker.addClass(cls);
        return cls;
    }
    default ClassChecker checkClass(ParserRuleContext ctx, String type) {
        ClassChecker classChecker = getClassChecker();
        String err = classChecker.checkClass(type);
        if(err != null){
            classChecker.addUnFoundClass(new ErrorContent(type, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(),err));
        }
        return classChecker;
    }

    default void logSppError(int line, int charPositionInLine, String msg) {
        System.err.println(getSppFile()+":" + line + ":" + (charPositionInLine+1) );
        System.err.println(msg);
    }
    default void logSppError(ParserRuleContext ctx, String msg) {
        if(msg != null)
            logSppError(ctx.getStart().getLine(),ctx.getStart().getCharPositionInLine(),msg);
    }

    default SppClass genObjectDecl(CompilationUnitType type, String objName) {
        return ClassChecker.genObjectDecl(type,objName);
    }

    default String createAParameter(String name) {
        return createAParameter(null, name);
    }

    default String createAParameter(String ptype, String name) {

        if (name == null) {
            name = firstToLowerCase(ptype, true);
        }
        return name;
    }

    default String firstToLowerCase(String ptype, boolean toLower) {
        String name;
        char first = ptype.charAt(0);
        first = toLower? Character.toLowerCase(first) : Character.toUpperCase(first);
        StringBuffer sb = new StringBuffer();
        name = sb.append(first).append(ptype.substring(1)).toString();
        return name;
    }

}
