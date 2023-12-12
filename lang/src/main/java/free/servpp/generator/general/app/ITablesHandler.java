package free.servpp.generator.general.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppCompilationUnit;
import free.servpp.generator.models.app.*;
import free.servpp.lang.antlr.AppParser;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author lidong@date 2023-12-05@version 1.0
 */
public interface ITablesHandler extends IApplicationHandler {
    @Override
    default void enterTables(AppParser.TablesContext ctx) {
        String name = ctx.getChild(1).getText();
        AppTables appTables = new AppTables(name);
        getCurrentRuleBlock().setCurrentAnnotatable(appTables);
        appTables.addToList(getCurrentRuleBlock().getAppTables(), appTables);
    }

    @Override
    default void exitTables(AppParser.TablesContext ctx) {

    }

    @Override
    default void enterTableDefine(AppParser.TableDefineContext ctx) {
        String name = ctx.getChild(1).getText();
        AppTable appTable = new AppTable(name);
        AppTables appTables = getLastElement(getCurrentRuleBlock().getAppTables());
        appTable.addToList(appTables.getAppTableList(), appTable);
    }

    @Override
    default void exitTableDefine(AppParser.TableDefineContext ctx) {

    }

    @Override
    default void enterIdentifyList(AppParser.IdentifyListContext ctx) {
    }

    @Override
    default void exitIdentifyList(AppParser.IdentifyListContext ctx) {
    }

    @Override
    default void enterQualifiedItemList(AppParser.QualifiedItemListContext ctx) {
    }

    @Override
    default void exitQualifiedItemList(AppParser.QualifiedItemListContext ctx) {
    }

    @Override
    default void enterQualifiedItem(AppParser.QualifiedItemContext ctx) {
        String name = ctx.getChild(0).getText();

        ParserRuleContext parent = ctx.getParent().getParent();
        AppTable appTable = getLastAppTable();
        for (SppClass sppClass : appTable.getEntities().getArrayList()) {
            SppFieldDefine sppFieldDefine = getQualifiedField(ctx, null, name);
            if (sppFieldDefine == null) {
                logSppError(ctx, "Field " + name + " not found.");
            } else {
                if (parent instanceof AppParser.PrimaryKeyDefineContext) {
                    appTable.getPrimaryKeys().add(sppFieldDefine);
                } else if (parent instanceof AppParser.ForeignKeyDefineContext) {
                    if(!appTable.containsEntity(sppFieldDefine.getSppClass().getName()))
                        logSppError(ctx, "Foreign key:" + sppFieldDefine.getSppField().getName() +" not defined in table entities.");
                    else
                        getLastElement(appTable.getForeignKeys()).getKeys().add(sppFieldDefine);
                }
            }
        }
    }

    @Override
    default void exitQualifiedItem(AppParser.QualifiedItemContext ctx) {
    }

    @Override
    default void enterIdentifyItem(AppParser.IdentifyItemContext ctx) {
        String name = ctx.getChild(0).getText();

//        ParserRuleContext parent = ctx.getParent().getParent();
//        if(parent instanceof AppParser.TableDefineContext){
        AppTable appTable = getLastAppTable();
        SppClass sppClass = (SppClass) getSppDomian().getSppClass(name);
        if (sppClass == null) {
            logSppError(ctx, "Entity " + name + " not found.");
            return;
        }
        appTable.getEntities().add(sppClass);
//        }
    }

    @Override
    default void exitIdentifyItem(AppParser.IdentifyItemContext ctx) {
    }

    @Override
    default void enterColumnDefine(AppParser.ColumnDefineContext ctx) {
        AppTable appTable = getLastAppTable();
        appTable.getAppColumns().add(new AppColumn());
    }

    @Override
    default void exitColumnDefine(AppParser.ColumnDefineContext ctx) {
    }

    @Override
    default void enterColumnNullalbe(AppParser.ColumnNullalbeContext ctx) {
        AppColumn appColumn = getLastAppColumn();
        appColumn.setNullable(true);
    }

    @Override
    default void exitColumnNullalbe(AppParser.ColumnNullalbeContext ctx) {
    }

    @Override
    default void enterColumnNotnull(AppParser.ColumnNotnullContext ctx) {
        getLastAppColumn().setNullable(false);
    }

    @Override
    default void exitColumnNotnull(AppParser.ColumnNotnullContext ctx) {
    }

    @Override
    default void enterColumnPrimaryKey(AppParser.ColumnPrimaryKeyContext ctx) {
        getLastAppColumn().setPrimaryKey(true);
    }

    @Override
    default void exitColumnPrimaryKey(AppParser.ColumnPrimaryKeyContext ctx) {
    }

    @Override
    default void enterColumnTypePrimitiveType(AppParser.ColumnTypePrimitiveTypeContext ctx) {
        String type = ctx.getChild(0).getText();
        SppClass sppClass = (SppClass) getSppDomian().getSppClass(type);
        getLastAppColumn().setType(sppClass);
    }

    @Override
    default void exitColumnTypePrimitiveType(AppParser.ColumnTypePrimitiveTypeContext ctx) {
    }

    @Override
    default void enterColumnTypePrecision(AppParser.ColumnTypePrecisionContext ctx) {
        int precesion = Integer.parseInt(ctx.getChild(0).getText());
        getLastAppColumn().setPrecision(precesion);
    }

    @Override
    default void exitColumnTypePrecision(AppParser.ColumnTypePrecisionContext ctx) {
    }

    @Override
    default void enterColumnTypeScale(AppParser.ColumnTypeScaleContext ctx) {
        int scale = Integer.parseInt(ctx.getChild(0).getText());
        getLastAppColumn().setScale(scale);
    }

    @Override
    default void exitColumnTypeScale(AppParser.ColumnTypeScaleContext ctx) {
    }

    @Override
    default void enterColumnTypePrecisionScale(AppParser.ColumnTypePrecisionScaleContext ctx) {
    }

    @Override
    default void exitColumnTypePrecisionScale(AppParser.ColumnTypePrecisionScaleContext ctx) {
    }

    @Override
    default void enterColumnName(AppParser.ColumnNameContext ctx) {
        getLastAppColumn().setName(ctx.getChild(0).getText());
    }

    @Override
    default void exitColumnName(AppParser.ColumnNameContext ctx) {
    }

    @Override
    default void enterColumnType(AppParser.ColumnTypeContext ctx) {
    }

    @Override
    default void exitColumnType(AppParser.ColumnTypeContext ctx) {
    }

    @Override
    default void enterKeyDefine(AppParser.KeyDefineContext ctx) {
    }

    @Override
    default void exitKeyDefine(AppParser.KeyDefineContext ctx) {
    }

    @Override
    default void enterPrimaryKeyDefine(AppParser.PrimaryKeyDefineContext ctx) {
    }

    @Override
    default void exitPrimaryKeyDefine(AppParser.PrimaryKeyDefineContext ctx) {
    }

    @Override
    default void enterForeignKeyDefine(AppParser.ForeignKeyDefineContext ctx) {
        AppForeign appForeign = new AppForeign();
        getLastAppTable().getForeignKeys().add(appForeign);
    }

    @Override
    default void exitForeignKeyDefine(AppParser.ForeignKeyDefineContext ctx) {
    }

    @Override
    default void enterForeignKeyReferences(AppParser.ForeignKeyReferencesContext ctx) {
        String id = ctx.getChild(1).getText();
        AppTable appTable = getLastAppTable();
        AppForeign appForeign = getLastElement(appTable.getForeignKeys());
        SppCompilationUnit sppClass = getSppDomian().getSppClass(id);
        if (sppClass == null) {
            logSppError(ctx, "Entity " + id + " not found.");
        } else
            appForeign.setForeignReference(sppClass);
    }

    @Override
    default void exitForeignKeyReferences(AppParser.ForeignKeyReferencesContext ctx) {
    }

    private AppColumn getLastAppColumn() {
        AppTable appTable = getLastAppTable();
        AppColumn appColumn = getLastElement(appTable.getAppColumns());
        return appColumn;
    }

    private AppTable getLastAppTable() {
        return getLastElement(getLastElement(getCurrentRuleBlock().getAppTables()).getAppTableList());
    }
}
