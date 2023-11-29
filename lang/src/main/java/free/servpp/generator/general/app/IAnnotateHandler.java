package free.servpp.generator.general.app;

import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.SppCompilationUnit;
import free.servpp.generator.models.app.AnnotationDefine;
import free.servpp.generator.models.app.AppAnnotation;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.lang.antlr.AppParser;

import java.util.List;

/**
 * @author lidong@date 2023-11-28@version 1.0
 */
public interface IAnnotateHandler extends IApplicationHandler,IRecursionProcess{
    @Override
    default void enterAnnotate(AppParser.AnnotateContext ctx){
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppAnnotation appAnnotation = new AppAnnotation();
        List<AppAnnotation> appAnnotationList = ruleBlock.getAppAnnotationList();
        if(appAnnotationList.size() == 0){
            ruleBlock.getAppAnnotations().addComponent(appAnnotation);
            setCurrentContainer(appAnnotation);
        }else{
            addRecursionComponent(appAnnotation);
        }
    }

    @Override
    default void exitAnnotate(AppParser.AnnotateContext ctx){
        exitCurrent();
    }

    @Override
    default void enterAnnotateDefine(AppParser.AnnotateDefineContext ctx) {
        String text = ctx.getChild(1).getText();
        AnnotationDefine annotationDefine = new AnnotationDefine(text);
        AppAnnotation annotation =(AppAnnotation)getCurrentContainer();
        annotationDefine = annotation.addAnnotationDefine(annotationDefine);
        if(annotationDefine == null ){
            logSppError(ctx,"Duplicate  annotation " +text);
        }
    }

    @Override
    default void exitAnnotateDefine(AppParser.AnnotateDefineContext ctx) {

    }

    @Override
    default void enterAnnotateParameter(AppParser.AnnotateParameterContext ctx){
        String name = ctx.getChild(0).getText();
        String value = ctx.getChild(2).getText();
        value = value.substring(1,value.length() -1);
        AppAnnotation annotation = (AppAnnotation) getCurrentContainer();
        AnnotationDefine annotationDefine = getLastElement(annotation.getAnnotationDefineList());
        annotationDefine.addParameter(name,value);
    }

    @Override
    default void exitAnnotateParameter(AppParser.AnnotateParameterContext ctx){

    }

    @Override
    default void enterAnnotateBody(AppParser.AnnotateBodyContext ctx){}

    @Override
    default void exitAnnotateBody(AppParser.AnnotateBodyContext ctx){}

    @Override
    default void enterAnnotateBodyIdentifier(AppParser.AnnotateBodyIdentifierContext ctx){
        String clsName = ctx.getChild(0).getText();
        SppCompilationUnit unit = getSppDomian().getSppClass(clsName);
        unit = unit == null ? getSppDomian().getSppClass(NameUtil.firstToLowerCase(clsName,false)) : unit;
        if(unit == null){
            logSppError(ctx,"Can not find annotated unit " +clsName);
        }else{
            AppAnnotation annotation =(AppAnnotation)getCurrentContainer();
            if(annotation.getUnits().contains(unit)){
                logSppError(ctx,"Duplicate unit " +clsName +" in annotation define.");
            }else {
                annotation.getUnits().add(unit);
            }
        }
    }

    @Override
    default void exitAnnotateBodyIdentifier(AppParser.AnnotateBodyIdentifierContext ctx){
    }

    @Override
    default void enterAnnotateParameterDefine(AppParser.AnnotateParameterDefineContext ctx){}

    @Override
    default void exitAnnotateParameterDefine(AppParser.AnnotateParameterDefineContext ctx){}
}
