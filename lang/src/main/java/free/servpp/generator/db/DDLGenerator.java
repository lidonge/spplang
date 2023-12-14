package free.servpp.generator.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import free.servpp.ILogable;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.ISppErrorLogger;
import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.*;
import free.servpp.generator.models.app.*;
import free.servpp.generator.util.NamedArray;

import java.io.File;
import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;

import static free.servpp.generator.db.DbTableConfig.*;

/**
 * @author lidong@date 2023-12-07@version 1.0
 */
public class DDLGenerator implements ISppErrorLogger {
    SppDomain sppDomain;
    NamedArray<DbTable> tableList = new NamedArray<>();

    public DDLGenerator(SppDomain sppDomain) {
        this.sppDomain = sppDomain;
        createTables();
        mapping(sppDomain);
    }

    private void mapping(SppDomain sppDomain) {
        for (AppMapper appMapper : sppDomain.getRuleBlock().getAppMappers()) {
            DbTable dbTable = tableList.get(appMapper.getName());
            for (MapperItem mapperItem : appMapper.getMapperItemList()) {
                String name = mapperItem.getName();
                mapColumn(mapperItem, dbTable.getColumns().get(name), dbTable, true);
                mapColumn(mapperItem, dbTable.getPrimaryKeys().get(name), dbTable, false);
                for (DbForeign dbForeign : dbTable.getDbForeigns()) {
                    mapColumn(mapperItem, dbForeign.getForeignKeys().get(name), dbTable, false);
                }
            }
        }
    }

    private void mapColumn(MapperItem mapperItem, DbColumn dbColumn, DbTable dbTable, boolean showError) {
        if (dbColumn == null) {
            if (showError)
                logSppError(mapperItem.getCtx(), "Column " + mapperItem.getName() + " not defined in table " + dbTable.getName());
        } else {
            SQlType sqlType = mapperItem.getSqlType();
            dbColumn.setName(mapperItem.getMapName() == null ? dbColumn.getName() : mapperItem.getMapName())
                    .setNullable(sqlType.isNotnull() == null ? dbColumn.isNullable() : !sqlType.isNotnull())
                    .setScale(sqlType.getScale() == null ? dbColumn.getScale() : sqlType.getScale())
                    .setPrecision(sqlType.getPrecision() == null ? dbColumn.getPrecision() : sqlType.getPrecision())
                    .setJdbcType(sqlType.getType() == null ? dbColumn.getJdbcType() : mapToJDBCType(sqlType.getType()));
        }
    }

    private void createTables() {
        for (AppTables appTables : sppDomain.getRuleBlock().getAppTables()) {
            DbTableConfig rootConfigs = readConfig(appTables.getAnnotations(), new DbTableConfig());
            for (AppTable appTable : appTables.getAppTableList()) {
                DbTableConfig configs = readConfig(appTable.getAnnotations(), rootConfigs.clone());
                DbTable dbTable = new DbTable(appTable);
                tableList.add(dbTable);
                addAppColumns(appTable, dbTable);
                for (SppFieldDefine pk : appTable.getPrimaryKeys()) {
                    SppField sppField = pk.getSppField();
                    if (sppField.getType().getType() == null) {
                        DbColumn dbColumn = createDbColumn(configs, sppField);
                        dbTable.addPrimaryKey(dbColumn);
                    }
                }
            }
            for (AppTable appTable : appTables.getAppTableList()) {
                DbTableConfig configs = readConfig(appTable.getAnnotations(), rootConfigs.clone());
                DbTable dbTable = tableList.get(appTable.getName());
                for (SppFieldDefine pk : appTable.getPrimaryKeys()) {
                    SppField sppField = pk.getSppField();
                    if (sppField.getType().getType() != null) {
                        DbTable outerTable = getDbTableByRoleName(sppField.getType().getName());
                        boolean hasSame = containsSameType(appTable, sppField);
                        for (DbColumn dbColumn : outerTable.getPrimaryKeys().getArrayList()) {
                            dbColumn = dbColumn.clone();
                            dbColumn.setName(genColumnName(configs, sppField, dbColumn.getName(),
                                    hasSame ? configs.getDupNamingType() : configs.getNamingType()));
                            dbTable.addPrimaryKey(dbColumn);
                        }
                    }
                }
                for (AppForeign appForeign : appTable.getForeignKeys()) {
                    genForeignKeys(configs, appForeign, dbTable);
                }
                addEntityColumns(appTable, configs, dbTable);
            }
        }
    }

    private void genForeignKeys(DbTableConfig configs, AppForeign appForeign, DbTable dbTable) {
        DbForeign dbForeign = new DbForeign();
        dbTable.addForeign(dbForeign);
        for (SppFieldDefine sppFieldDefine : appForeign.getKeys()) {
            createForeignDbColumns(configs, dbTable, dbForeign, sppFieldDefine.getSppField());
        }
    }

    private void addAppColumns(AppTable appTable, DbTable dbTable) {
        for (AppColumn appColumn : appTable.getAppColumns()) {
            DbColumn dbColumn = new DbColumn();
            dbColumn.setName(appColumn.getName()).setJdbcType(mapToJDBCType(appColumn.getType()))
                    .setPrecision(appColumn.getPrecision()).setScale(appColumn.getScale())
                    .setNullable(appColumn.isNullable());
            dbTable.remove(dbColumn);
            dbTable.addComponent(dbColumn);
            if (appColumn.isPrimaryKey()) {
                dbTable.addPrimaryKey(dbColumn);
            }
        }
    }

    private boolean containsSameType(AppTable appTable, SppLocalVar localVar) {
        int count = 0;
        for (SppClass sppClass : appTable.getEntities().getArrayList()) {
            for (SppLocalVar sppLocalVar : sppClass.getSppFieldList()) {
                if (sppLocalVar.getType().getName().equals(localVar.getType().getName()) &&
                        sppLocalVar.getArrayDimension() == localVar.getArrayDimension()) {
                    count++;
                    if (count > 1)
                        return true;
                }
            }
        }
        return false;
    }

    private void addEntityColumns(AppTable appTable, DbTableConfig configs, DbTable dbTable) {
        for (SppClass sppClass : appTable.getEntities().getArrayList()) {
            for (SppLocalVar sppLocalVar : sppClass.getSppFieldList()) {
                if (sppLocalVar.getArrayDimension() != 0)
                    continue;
                if (sppLocalVar.getType().getType() == null) {
                    DbColumn dbColumn = createDbColumn(configs, sppLocalVar);
                    dbTable.addComponent(dbColumn);
                } else {
                    DbTable ref = null;
                    if (dbTable.getDbForeigns() != null) {
                        for (DbForeign dbForeign : dbTable.getDbForeigns()) {
                            if (dbForeign.getReference().getAppTable().containsEntity(sppLocalVar.getType().getName())) {
                                ref = dbForeign.getReference();
                                break;
                            }
                        }
                    }
                    boolean hasSame = containsSameType(appTable, sppLocalVar);
                    if (ref == null) {//foreign key not exists, expand the entity
                        SppClass refClass = (SppClass) sppLocalVar.getType();
                        for (SppLocalVar filed : refClass.getSppFieldList()) {
                            DbColumn dbColumn = createDbColumn(configs, filed);
                            dbColumn.setName(genColumnName(configs, sppLocalVar, filed.getName(),
                                    hasSame ? configs.getDupNamingType() : configs.getNamingType()));
                            dbTable.addComponent(dbColumn);
                        }
                    } else {
                        for (DbColumn dbColumn : ref.getPrimaryKeys().getArrayList()) {
                            DbColumn col = dbColumn.clone();
                            col.setName(genColumnName(configs, sppLocalVar, col.getName(),
                                    hasSame ? configs.getDupNamingType() : configs.getNamingType()));
                            dbTable.addComponent(col);
                        }
                    }
                }
            }
        }
    }

    private DbColumn createDbColumn(DbTableConfig configs, SppLocalVar sppLocalVar) {
        DbColumn dbColumn = new DbColumn()
                .setName(sppLocalVar.getName())
                .setJdbcType(mapToJDBCType(sppLocalVar.getType()));
        dbColumn.setJdbcType(changeType(dbColumn, configs)).setNullable(configs.isNullable());
        setPrecisionScale(dbColumn, configs);
        return dbColumn;
    }

    private DbTableConfig readConfig(List<AnnotationDefine> annotationDefines, DbTableConfig config) {
        if (annotationDefines == null)
            return config;
        for (AnnotationDefine annotationDefine : annotationDefines) {
            String name = annotationDefine.getName();
            if (COLUMN.equals(name)) {
                ObjectMapper objectMapper = new ObjectMapper();
                DbTableConfig config1 = objectMapper.convertValue(annotationDefine.getParameters(), DbTableConfig.class);
                config.copy(config1);
            } else if (INSIDE_ENTITY.equals(name)) {
                DbTableConfig.NamingType namingType = DbTableConfig.NamingType.valueOf(annotationDefine.getParameters().get(NAMING_TYPE));
                config.setNamingType(namingType);
                String dup = annotationDefine.getParameters().get(DUP_NAMING_TYPE);
                if (dup != null) {
                    DbTableConfig.NamingType dupNamingType = DbTableConfig.NamingType.valueOf(dup);
                    config.setDupNamingType(dupNamingType);
                }
            }
        }
        return config;
    }

    private void setPrecisionScale(DbColumn dbColumn, DbTableConfig config) {
        switch (dbColumn.getJdbcType()) {
            case VARCHAR:
                dbColumn.setPrecision(config.getVarchar_precision());
                break;
            case BIGINT:
                dbColumn.setPrecision(config.getBigint_precision());
                break;
            case DOUBLE:
            case NUMERIC:
            case DECIMAL:
                dbColumn.setPrecision(config.getDecimal_precision());
                dbColumn.setScale(config.getDecimal_scale());
                break;
        }
    }

    private JDBCType changeType(DbColumn dbColumn, DbTableConfig config) {
        //because spptype is converted by default, so use jdbctype as spptype
        JDBCType jdbcType = dbColumn.getJdbcType();
        switch (jdbcType) {
            case BOOLEAN:
                jdbcType = config.getType_boolean();
                break;
            case VARCHAR:
                jdbcType = config.getType_String();
                break;
//            default://TODO
//                throw new RuntimeException("type " + jdbcType + " not supported.");
        }
        return jdbcType;
    }
//    private int precision(SppLocalVar sppLocalVar)

    private JDBCType mapToJDBCType(SppCompilationUnit sppClass) {
        return mapToJDBCType(sppClass.getName());
    }

    private JDBCType mapToJDBCType(String sppType) {
        int index = Arrays.stream(IConstance.primaryTypes).toList().indexOf(sppType);
        return IConstance.jdbcTypes[index];
    }

    private String genColumnName(DbTableConfig configs, SppLocalVar sppLocalVar, String colName, DbTableConfig.NamingType namingType) {
        String columnName = NameUtil.firstToLowerCase(sppLocalVar.getName(), false) +
                NameUtil.firstToLowerCase(colName, false);//default PascalCase
        switch (namingType) {
            case snake_case:
                columnName = sppLocalVar.getName() + "_" + colName;
                columnName = columnName.toLowerCase();
                break;
            case lowercase:
                columnName = colName.toLowerCase();
                break;
            case UPPERCASE:
                columnName = colName.toUpperCase();
                break;
            case camelCase:
                columnName = NameUtil.firstToLowerCase(columnName, true);
                break;
            case PascalCase:
                break;
            case pascallowercase:
                columnName = columnName.toLowerCase();
                break;
            case PASCALUPPERCASE:
                columnName = columnName.toUpperCase();
                break;
        }
        return columnName;
    }

    private void createForeignDbColumns(DbTableConfig configs, DbTable dbTable, DbForeign dbForeign, SppLocalVar sppLocalVar) {
        String foreignRoleName = sppLocalVar.getType().getName();
        DbTable foreignTable = getDbTableByRoleName(foreignRoleName);
        boolean hasSame = containsSameType(dbTable.getAppTable(), sppLocalVar);
        for (DbColumn dbColumn : foreignTable.getPrimaryKeys().getArrayList()) {
            DbRefColumn dbRefColumn = new DbRefColumn(dbColumn);
            dbRefColumn.setRefName(dbColumn.getName());
            dbRefColumn.setName(genColumnName(configs, sppLocalVar, dbRefColumn.getName(),
                    hasSame ? configs.getDupNamingType() : configs.getNamingType()));
            dbForeign.setReference(foreignTable);
            dbTable.addForeignKey(dbForeign, dbRefColumn);
        }
    }

    private DbTable getDbTableByRoleName(String foreignRoleName) {
        DbTable ret = null;
        for (DbTable theTable : tableList.getArrayList()) {
            if (theTable.getAppTable().containsEntity(foreignRoleName)) {
                ret = theTable;
                break;
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "DDLGenerator{" +
                "tableList=" + tableList +
                '}';
    }

    @Override
    public File getAntlrFile() {
        return null;
    }
}

