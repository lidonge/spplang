package free.servpp.generator.general.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.app.*;
import free.servpp.lang.antlr.AppParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public interface IMapperHandler extends IApplicationHandler {
    @Override
    default void enterMap(AppParser.MapContext ctx){
        String tableName = ctx.getChild(1).getText();
        AppTable sppClass = getCurrentRuleBlock().getAllTables().get(tableName);
        if(sppClass == null){
            logSppError(ctx, "Identifier " + tableName + " not defined.");
        }else {
            AppMapper appMapper = new AppMapper(sppClass);
            RuleBlock currentRuleBlock = getCurrentRuleBlock();
            currentRuleBlock.addMapper(appMapper);
            currentRuleBlock.setCurrentAnnotatable(appMapper);
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
        String id = ctx.getChild(0).getText();
        RuleBlock ruleBlock = getCurrentRuleBlock();
        AppMapper appMapper = getLastElement(ruleBlock.getAppMappers());
        MapperItem mapperItem = new MapperItem(id, ctx);
        appMapper.addMapperItem(mapperItem);

        List<TerminalNode> tokens = ctx.getTokens(AppParser.Identifier);
        if(tokens.size() ==2 ){
            String mapName = tokens.get(1).getText();
            mapperItem.setMapName(mapName);
        }
    }

    @Override
    default void enterSqlTypeNotnull(AppParser.SqlTypeNotnullContext ctx){
        MapperItem mapperItem = getMapperItem();
        mapperItem.getSqlType().setNotnull(true);
    }

    @Override
    default void exitSqlTypeNotnull(AppParser.SqlTypeNotnullContext ctx) {

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
