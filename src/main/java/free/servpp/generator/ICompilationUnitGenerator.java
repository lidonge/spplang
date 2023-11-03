package free.servpp.generator;

import free.servpp.SppListener;
import free.servpp.generator.checker.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface ICompilationUnitGenerator extends SppListener,IConstance {
    File getDomainPath();

    String getSppFile();

    ClassChecker getClassChecker();

    void push(Object obj);
    Object pop();
    Object peek();

    int stackSize();
//    Stack getStack();
    default String generateLocalVar(String ptype, String name) {
        return _generateField(ptype,name,true);
    }
    default String generateField(String ptype, String name) {
        return  _generateField(ptype,name,false);
    }
    private String _generateField(String ptype, String name, boolean bLocal) {
        ClassChecker checker = getClassChecker();
        SppClass typeClass = checker.getSppClass(ptype);
        if(typeClass == null)
            typeClass = new SppClass(ptype);
        if(bLocal)
            return checker.getCurrentClass().addLocalVar(new SppLocalVar(typeClass, name));
        else
            return checker.getCurrentClass().addField(new SppField(typeClass, name));
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
        glbChecker.addClass(cls);
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
        if(type == CompilationUnitType.atomicservice | type == CompilationUnitType.scenario)
            return new SppService(objName);
        return new SppClass(objName);
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
