package free.servpp.generator.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.*;
import free.servpp.generator.models.app.*;
import free.servpp.generator.util.NamedArray;

import java.sql.JDBCType;
import java.util.Arrays;

/**
 * @author lidong@date 2023-12-07@version 1.0
 */
public class DDLGenerator {
    public static final String NAMING_TYPE = "namingType";
    public static final String INSIDE_ENTITY = "insideEntity";
    public static final String COLUMN = "column";
    SppDomain sppDomain;
    NamedArray<DbTable> tableList = new NamedArray<>();

    public DDLGenerator(SppDomain sppDomain) {
        this.sppDomain = sppDomain;
        createTables();
    }

    private void createTables() {
        for (AppTables appTables : sppDomain.getRuleBlock().getAppTables()) {
            DbTableConfig config = new DbTableConfig();
            DbTableConfig.NamingType namingType = DbTableConfig.NamingType.snake_case;
            Configs configs = readConfig(appTables, config, namingType);
            for (AppTable appTable : appTables.getAppTableList()) {
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
                DbTable dbTable = tableList.get(appTable.getName());
                for (SppFieldDefine pk : appTable.getPrimaryKeys()) {
                    SppField sppField = pk.getSppField();
                    if (sppField.getType().getType() != null) {
                        DbTable outerTable = getDbTableByRoleName(sppField.getType().getName());
                        for(DbColumn dbColumn : outerTable.getPrimaryKeys().getArrayList()){
                            dbColumn = dbColumn.clone();
                            dbColumn.setName(genColumnName(configs,sppField, dbColumn.getName(),configs.namingType()));
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

    private void genForeignKeys(Configs configs, AppForeign appForeign, DbTable dbTable) {
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
                    .setPrecision(appColumn.getPrecision()).setScale(appColumn.getScale());
            dbTable.remove(dbColumn);
            dbTable.addComponent(dbColumn);
            if (appColumn.isPrimaryKey())
                dbTable.addPrimaryKey(dbColumn);
        }
    }

    private void addEntityColumns(AppTable appTable, Configs configs, DbTable dbTable) {
        for (SppClass sppClass : appTable.getEntities().getArrayList()) {
            for (SppLocalVar sppLocalVar : sppClass.getSppFieldList()) {
                if(sppLocalVar.getArrayDimension() !=0)
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
                    if (ref == null) {
                        SppClass refClass = (SppClass) sppLocalVar.getType();
                        for (SppLocalVar filed : refClass.getSppFieldList()) {
                            DbColumn dbColumn = createDbColumn(configs, filed);
                            dbColumn.setName(genColumnName(configs, sppLocalVar, filed.getName(), configs.namingType()));
                            dbTable.addComponent(dbColumn);
                        }
                    } else {
                        for (DbColumn dbColumn : ref.getPrimaryKeys().getArrayList()) {
                            DbColumn col = dbColumn.clone();
                            col.setName(genColumnName(configs, sppLocalVar, col.getName(), configs.namingType()));
                            dbTable.addComponent(col);
                        }
                    }
                }
            }
        }
    }

    private DbColumn createDbColumn(Configs configs, SppLocalVar sppLocalVar) {
        DbColumn dbColumn = new DbColumn()
                .setName(sppLocalVar.getName())
                .setJdbcType(mapToJDBCType(sppLocalVar.getType()));
        dbColumn.setJdbcType(changeType(dbColumn, configs.config())).setNullable(configs.config().isNullable());
        setPrecisionScale(dbColumn, configs.config());
        return dbColumn;
    }

    private Configs readConfig(AppTables appTables, DbTableConfig config, DbTableConfig.NamingType namingType) {
        for (AnnotationDefine annotationDefine : appTables.getAnnotations()) {
            String name = annotationDefine.getName();
            if (COLUMN.equals(name)) {
                ObjectMapper objectMapper = new ObjectMapper();
                config = objectMapper.convertValue(annotationDefine.getParameters(), DbTableConfig.class);
            } else if (INSIDE_ENTITY.equals(name)) {
                namingType = DbTableConfig.NamingType.valueOf(annotationDefine.getParameters().get(NAMING_TYPE));
            }
        }
        Configs configs = new Configs(config, namingType);
        return configs;
    }

    private record Configs(DbTableConfig config, DbTableConfig.NamingType namingType) {
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
        String sppType = sppClass.getName();
        int index = Arrays.stream(IConstance.primaryTypes).toList().indexOf(sppType);
        return IConstance.jdbcTypes[index];
    }

    private String genColumnName(Configs configs, SppLocalVar sppLocalVar, String colName, DbTableConfig.NamingType namingType) {
        String columnName = NameUtil.firstToLowerCase(sppLocalVar.getName(), false) +
                NameUtil.firstToLowerCase(colName, false);//default PascalCase
        switch (namingType) {
            case snake_case:
                columnName = sppLocalVar.getName() + "_" + colName;
                columnName = columnName.toLowerCase();
                break;
            case lowercase:
                columnName = columnName.toLowerCase();
                break;
            case UPPERCASE:
                columnName = columnName.toUpperCase();
                break;
            case camelCase:
                columnName = NameUtil.firstToLowerCase(columnName, true);
                break;
            case PascalCase:
                break;
        }
        return columnName;
    }

    private void createForeignDbColumns(Configs configs, DbTable dbTable, DbForeign dbForeign, SppLocalVar sppLocalVar) {
        String foreignRoleName = sppLocalVar.getType().getName();
        DbTable foreignTable = getDbTableByRoleName(foreignRoleName);
        for (DbColumn dbColumn : foreignTable.getPrimaryKeys().getArrayList()) {
            DbRefColumn dbRefColumn = new DbRefColumn(dbColumn);
            dbRefColumn.setRefName(dbColumn.getName());
            dbRefColumn.setName(genColumnName(configs, sppLocalVar, dbRefColumn.getName(), configs.namingType()));
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
}

