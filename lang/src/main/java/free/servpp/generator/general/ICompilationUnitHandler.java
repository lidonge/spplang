package free.servpp.generator.general;

import free.servpp.generator.models.*;
import free.servpp.lang.antlr.SppListener;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface ICompilationUnitHandler extends SppListener, IConstance, ISppErrorLogger {
    SppDomain getSppDomain();
    void setSppDomain(SppDomain sppDomain);

    void push(Object obj);
    Object pop();
    Object peek();

    int stackSize();
    default <T> T getLastElement(List<T> elements){
        return elements.get(elements.size()-1);
    }
    default <T> T removeLastElement(List<T> elements){
        return elements.remove(elements.size()-1);
    }
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
        SppDomain checker = getSppDomain();
        SppCompilationUnit typeClass =  checker.getSppClass(CompilationUnitType.entity, ptype);
        SppLocalVar var = maker.create(typeClass,name);
        if(bLocal)
            return ((SppService)checker.getCurrentClass()).addLocalVar(var);
        else
            return ((SppClass)checker.getCurrentClass()).addField((SppField) var);
    }

    default SppService generateService(CompilationUnitType type, ParserRuleContext ctx, String objName, String methodName) {
        SppService ret = (SppService) generateClass(type,ctx,objName);
        ret.setFuncName(methodName);
        return ret;
    }
    default SppClass generateClass(CompilationUnitType type, ParserRuleContext ctx, String objName) {
        SppDomain glbChecker = getSppDomain();
        String err = glbChecker.checkDupClass(objName);
        if(err != null){
            logSppError(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(),err);
        }
        SppClass cls = genObjectDecl(type, objName);
        cls.setType(type);
        cls = (SppClass) glbChecker.addClass(cls);
        return cls;
    }
    default SppDomain checkClass(ParserRuleContext ctx, String type) {
        SppDomain sppDomain = getSppDomain();
        String err = sppDomain.checkClass(type);
        if(err != null){
            sppDomain.addUnFoundClass(new ErrorContent(type, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(),err));
        }
        return sppDomain;
    }

    default SppClass genObjectDecl(CompilationUnitType type, String objName) {
        return SppDomain.genObjectDecl(type,objName);
    }

    default String createAParameter(String name) {
        return createAParameter(null, name);
    }

    default String createAParameter(String ptype, String name) {

        if (name == null) {
            name = NameUtil.firstToLowerCase(ptype, true);
        }
        return name;
    }


}