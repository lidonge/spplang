package free.servpp.generator.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.ISppErrorLogger;
import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.*;
import free.servpp.generator.models.app.*;
import free.servpp.generator.util.NamedArray;

import java.io.*;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static free.servpp.generator.db.DbTableConfig.*;

/**
 * @author lidong@date 2023-12-07@version 1.0
 */
public class DDLGenerator implements ISppErrorLogger {
    private SppDomain sppDomain;
    private NamedArray<DbTable> tableList = new NamedArray<>();

    public DDLGenerator(SppDomain sppDomain) {
        this.sppDomain = sppDomain;
        createTables();
        mapping(sppDomain);
    }

    public NamedArray<DbTable> getTableList() {
        return tableList;
    }

    private void mapping(SppDomain sppDomain) {
        for (AppMapper appMapper : sppDomain.getRuleBlock().getAppMappers()) {
            DbTable dbTable = tableList.get(appMapper.getName());
            for (MapperItem mapperItem : appMapper.getMapperItemList()) {
                String name = mapperItem.getName();
                mapColumn(mapperItem, dbTable.getColumns().get(name), dbTable, true);
                mapColumn(mapperItem, dbTable.getPrimaryKeys().get(name), dbTable, false);
                for(MybatisField mybatisField:dbTable.getMybatisClass().getFields()){
                    if(name.equals(mybatisField.getColumn())){
                        mybatisField.setColumn( name);
                    }
                }
                for (DbForeign dbForeign : dbTable.getDbForeigns()) {
                    mapColumn(mapperItem, dbForeign.getForeignKeys().get(name), dbTable, false);
                }
            }
            mappingMybatis(appMapper, dbTable.getMybatisClass().getFields());
        }
    }

    private static void mappingMybatis(AppMapper appMapper, List<MybatisField> mybatisFields) {
        for(MybatisField mybatisField: mybatisFields) {
            for (MapperItem mapperItem : appMapper.getMapperItemList()) {
                String name = mapperItem.getName();
                if(mybatisField instanceof IMybatisClass){
                    mappingMybatis(appMapper,((IMybatisClass) mybatisField).getFields());
                } else if (mybatisField instanceof MybatisRef) {
                    for(Map.Entry entry:((MybatisRef) mybatisField).foreignKeys){
                        if(name.equals(entry.getValue()))
                            entry.setValue(mapperItem.getMapName());
                    }
                } else if(name.equals(mybatisField.getColumn())){
                    mybatisField.setColumn( name);
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
                addPrimaryKeys(appTable, configs, dbTable);
            }
            for (AppTable appTable : appTables.getAppTableList()) {
                DbTableConfig configs = readConfig(appTable.getAnnotations(), rootConfigs.clone());
                DbTable dbTable = tableList.get(appTable.getName());
                genPrimaryKeyWhichIsTable(appTable, configs, dbTable);
                for (AppForeign appForeign : appTable.getForeignKeys()) {
                    genForeignKeys(configs, appForeign, dbTable);
                }
                addEntityColumns(appTable, configs, dbTable);
            }
        }
    }

    /**
     * If pk is not a primary type, use the primary key of the entity as pk.
     *
     * @param appTable
     * @param configs
     * @param dbTable
     */
    private void genPrimaryKeyWhichIsTable(AppTable appTable, DbTableConfig configs, DbTable dbTable) {
        for (SppFieldDefine pk : appTable.getPrimaryKeys()) {
            SppField sppField = pk.getSppField();
            if (sppField.getType().getType() != null) {
                DbTable outerTable = getDbTableByRoleName(sppField.getType().getName());
                //should not have same type pk
                boolean hasSame = containsSameType(appTable.getEntity(), sppField);
                for (DbColumn dbColumn : outerTable.getPrimaryKeys().getArrayList()) {
                    dbColumn = dbColumn.clone();
                    dbColumn.setName(genColumnName(configs, sppField.getName(), dbColumn.getName(),
                            hasSame ? configs.getDupNamingType() : configs.getNamingType()));
                    dbTable.addPrimaryKey(dbColumn);
                }
            }
        }
    }

    private void addPrimaryKeys(AppTable appTable, DbTableConfig configs, DbTable dbTable) {
        for (SppFieldDefine pk : appTable.getPrimaryKeys()) {
            SppField sppField = pk.getSppField();
            if (sppField.getType().getType() == null) {
                DbColumn dbColumn = createDbColumn(configs, sppField);
                dbTable.addPrimaryKey(dbColumn);
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
        SppClass sppClass = appTable.getEntity();
        for (AppColumn appColumn : appTable.getAppColumns()) {
            SppField field = new SppField(appColumn.getType(), appColumn.getName());
            sppClass.addField(field);
            DbColumn dbColumn = new DbColumn();
            dbColumn.setName(appColumn.getName()).setJdbcType(mapToJDBCType(appColumn.getType()))
                    .setPrecision(appColumn.getPrecision()).setScale(appColumn.getScale())
                    .setNullable(appColumn.isNullable())
                    .setField(field);
            dbTable.remove(dbColumn);
            dbTable.addComponent(dbColumn);
            if (appColumn.isPrimaryKey()) {
                dbTable.addPrimaryKey(dbColumn);
            } else
                dbTable.getMybatisClass().addField(new MybatisField(field.getName(), dbColumn.getName()));
        }
    }

    /**
     * Test if there are several field with same type, {Address ship; Address bill} e.g.
     *
     * @param sppClass
     * @param localVar
     * @return
     */
    private boolean containsSameType(SppClass sppClass, SppLocalVar localVar) {
        int count = 0;
        for (SppLocalVar sppLocalVar : sppClass.getSppFieldList()) {
            if (sppLocalVar.getType().getName().equals(localVar.getType().getName()) &&
                    sppLocalVar.getArrayDimension() == localVar.getArrayDimension()) {
                count++;
                if (count > 1)
                    return true;
            }
        }

        return false;
    }

    private void addEntityColumns(AppTable appTable, DbTableConfig configs, DbTable dbTable) {
        SppClass sppClass = appTable.getEntity();
        ArrayList<SppLocalVar> refs = new ArrayList<>();
        for (SppLocalVar sppLocalVar : sppClass.getSppFieldList()) {
            if (sppLocalVar.getArrayDimension() != 0)
                continue;
            if (sppLocalVar.getType().getType() == null) {//primary type field
                DbColumn dbColumn = createDbColumn(configs, sppLocalVar);
                dbTable.addComponent(dbColumn);
                boolean inPrimaryKeys = false;
                inPrimaryKeys = isInPrimaryKeys(dbTable, sppLocalVar, inPrimaryKeys);
                if(!inPrimaryKeys)
                    dbTable.getMybatisClass().addField(new MybatisField(sppLocalVar.getName(), dbColumn.getName()));
            } else {//Entity type field
                DbTable ref = getColumnReferredForeignTable(dbTable, sppLocalVar);
                boolean hasSame = containsSameType(appTable.getEntity(), sppLocalVar);
                if (ref == null) {//foreign key not exists, expand the entity
                    SppClass refClass = (SppClass) sppLocalVar.getType();
                    MybatisAssociation mybatisAssociation = getMybatisAssociation(dbTable.getMybatisClass(),sppLocalVar.getName(),refClass);
                    expandRefEntity(configs, dbTable, mybatisAssociation, sppLocalVar.getName(), refClass, hasSame);
                } else {
                    genRefEntityWithItsPrimaryKeys(configs, dbTable, sppLocalVar, ref, hasSame);
                }
            }
        }
        //move ref to the end of children, for recursion of mustache
        List<MybatisField> fields = dbTable.getMybatisClass().getFields();
        for(Object obj: fields.toArray()){
            MybatisField field = (MybatisField) obj;
            if(field instanceof MybatisRef
                || field instanceof MybatisAssociation){
                fields.remove(field);
                fields.add(field);
            }
        }
    }

    private static boolean isInPrimaryKeys(DbTable dbTable, SppLocalVar sppLocalVar, boolean inPrimaryKeys) {
        for(DbColumn pk: dbTable.getPrimaryKeys().getArrayList()){
            if(pk.getField().getName().equals(sppLocalVar.getName())){
                inPrimaryKeys = true;
                break;
            }
        }
        return inPrimaryKeys;
    }

    private static DbTable getColumnReferredForeignTable(DbTable dbTable, SppLocalVar sppLocalVar) {
        DbTable ref = null;
        if (dbTable.getDbForeigns() != null) {
            for (DbForeign dbForeign : dbTable.getDbForeigns()) {
                if (dbForeign.getReference().getAppTable().containsEntity(sppLocalVar.getType().getName())) {
                    ref = dbForeign.getReference();
                    break;
                }
            }
        }
        return ref;
    }

    private void genRefEntityWithItsPrimaryKeys(DbTableConfig configs, DbTable dbTable, SppLocalVar sppLocalVar, DbTable ref, boolean hasSame) {
        MybatisRef mybatisRef = new MybatisRef(sppLocalVar.getName(), null);
        mybatisRef.setRefName(sppLocalVar.getType().getName());
        dbTable.getMybatisClass().addField(mybatisRef);
//        if(dbTable.getAppTable().getName().equals("PRODUCT"))
//            dbTable = dbTable;
        for (DbColumn dbColumn : ref.getPrimaryKeys().getArrayList()) {
            DbColumn col = dbColumn.clone();
            col.setName(genColumnName(configs, sppLocalVar.getName(), col.getName(),
                    hasSame ? configs.getDupNamingType() : configs.getNamingType()));
            dbTable.addComponent(col);
            mybatisRef.addKeyPair(col.getField().getName(), col.getName());
        }
    }

    private void expandRefEntity(DbTableConfig configs, DbTable dbTable, IMybatisClass mybatisClass, String superName, SppClass refClass, boolean hasSame) {
        for (SppLocalVar field : refClass.getSppFieldList()) {
            String colName = genColumnName(configs, superName, field.getName(),
                    hasSame ? configs.getDupNamingType() : configs.getNamingType());
            if (field.getType().getType() == null) {//primary type
                DbColumn dbColumn = createDbColumn(configs, field);
                dbColumn.setName(colName);
                dbTable.addComponent(dbColumn);
                mybatisClass.addField(new MybatisField(field.getName(), dbColumn.getName()));
            } else {
                SppClass sppClass = (SppClass) field.getType();
                MybatisAssociation mybatisAssociation = getMybatisAssociation(mybatisClass, field.getName(), sppClass);
                expandRefEntity(configs, dbTable, mybatisAssociation, colName, sppClass, containsSameType(refClass, field));
            }
        }
    }

    private MybatisAssociation getMybatisAssociation(IMybatisClass mybatisClass, String fieldName, SppClass sppClass) {
        MybatisAssociation mybatisAssociation = new MybatisAssociation(fieldName, null);
        mybatisAssociation.setEntityName(sppClass.getName());
        mybatisClass.addField(mybatisAssociation);
        return mybatisAssociation;
    }

    private DbColumn createDbColumn(DbTableConfig configs, SppLocalVar sppLocalVar) {
        DbColumn dbColumn = new DbColumn()
                .setName(sppLocalVar.getName())
                .setJdbcType(mapToJDBCType(sppLocalVar.getType()))
                .setField(sppLocalVar);
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

    private String genColumnName(DbTableConfig configs, String superName, String colName, DbTableConfig.NamingType namingType) {
        String columnName = NameUtil.firstToLowerCase(superName, false) +
                NameUtil.firstToLowerCase(colName, false);//default PascalCase
        switch (namingType) {
            case snake_case:
                columnName = superName + "_" + colName;
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
        boolean hasSame = containsSameType(dbTable.getAppTable().getEntity(), sppLocalVar);
        for (DbColumn dbColumn : foreignTable.getPrimaryKeys().getArrayList()) {
            DbRefColumn dbRefColumn = new DbRefColumn(dbColumn);
            dbRefColumn.setRefName(dbColumn.getName());
            dbRefColumn.setOrigName(sppLocalVar.getName());
            dbRefColumn.setName(genColumnName(configs, sppLocalVar.getName(), dbRefColumn.getName(),
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

