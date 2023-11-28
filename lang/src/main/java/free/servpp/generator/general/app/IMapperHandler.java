package free.servpp.generator.general.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppCompilationUnit;
import free.servpp.generator.models.SppField;
import free.servpp.generator.models.app.AppMapper;
import free.servpp.generator.models.app.MapperItem;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.SppFieldDefine;
import free.servpp.lang.antlr.AppParser;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public interface IMapperHandler extends IApplicationHandler {
    @Override
    default void enterMap(AppParser.MapContext ctx){
        String clsName = ctx.getChild(1).getText();
        SppCompilationUnit sppClass = getSppDomian().getSppClass(clsName);
        if(sppClass == null){
            logSppError(ctx, "Identifier " + clsName + " not defined.");
        }else {
            getCurrentRuleBlock().addMapper(new AppMapper(sppClass));
        }
    }

    @Override
    default void enterMapParameterIdentifier(AppParser.MapParameterIdentifierContext ctx){
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppMapper appMapper = getLastElement(ruleBlock.getAppMappers());
        appMapper.addParameter(ctx.getChild(0).getText());
    }

    @Override
    default void enterMapItem(AppParser.MapItemContext ctx){
        String qualifiedName = ctx.getChild(0).getText();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppMapper appMapper = getLastElement(ruleBlock.getAppMappers());
        SppFieldDefine sppField = getQualifiedField(ctx, (SppClass) appMapper.getSppClass(), qualifiedName);
        if(sppField == null){
            logSppError(ctx, "Field " + qualifiedName + " not defined");
        }else{
            appMapper.addMapperItem(new MapperItem(qualifiedName,sppField));
        }
    }

    @Override
    default void enterNotnull(AppParser.NotnullContext ctx){
        MapperItem mapperItem = getMapperItem();
        mapperItem.getSqlType().setNotnull(true);
    }

    @Override
    default void enterSqlTypePrimitiveType(AppParser.SqlTypePrimitiveTypeContext ctx){
        MapperItem mapperItem = getMapperItem();
        mapperItem.getSqlType().setType(ctx.getChild(0).getText());
    }

    @Override
    default void enterPrecision(AppParser.PrecisionContext ctx){
        MapperItem mapperItem = getMapperItem();
        mapperItem.getSqlType().setPrecision(Integer.parseInt(ctx.getChild(0).getText()));
    }

    @Override
    default void enterScale(AppParser.ScaleContext ctx){
        MapperItem mapperItem = getMapperItem();
        mapperItem.getSqlType().setScale(Integer.parseInt(ctx.getChild(0).getText()));
    }

    private MapperItem getMapperItem() {
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppMapper appMapper = getLastElement(ruleBlock.getAppMappers());
        MapperItem mapperItem = getLastElement(appMapper.getMapperItemList());
        return mapperItem;
    }
}
