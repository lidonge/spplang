package free.servpp.generator.db;

import free.servpp.generator.models.IComponent;
import free.servpp.generator.models.IContainer;
import free.servpp.generator.models.SppLocalVar;
import free.servpp.generator.models.app.AppColumn;
import free.servpp.generator.models.app.INamedObject;

import java.sql.JDBCType;

/**
 * @author lidong@date 2023-12-07@version 1.0
 */
public class DbColumn<T extends IContainer> implements IComponent, INamedObject {
    private String name;
    private JDBCType jdbcType;
    private int precision;
    private int scale;

    private boolean nullable = true;

    DbTable parent;

    SppLocalVar field;

    public DbColumn() {
    }

    public DbColumn clone(){
        return new DbColumn(name,jdbcType,precision,scale,nullable,field);
    }
    protected DbColumn(String name, JDBCType jdbcType, int precision, int scale, boolean nullable, SppLocalVar field) {
        this.name = name;
        this.jdbcType = jdbcType;
        this.precision = precision;
        this.scale = scale;
        this.nullable = nullable;
        this.field = field;
    }

    public DbTable getTable(){
        return parent;
    }
    public SppLocalVar getField() {
        return field;
    }

    public DbColumn<T> setField(SppLocalVar field) {
        this.field = field;
        return this;
    }

    //for mustache
    public String getNullString(){
        return isNullable()? "null": "not null";
    }

    private String getTypeString(){
        String ret = jdbcType.getName();
        switch (jdbcType){
            case DOUBLE:
                ret = "BigDecimal";
                break;
        }
        return ret;
    }
    public String getPrecisionScale(){
        String ret = null;
        switch (jdbcType){
            case DOUBLE:
            case NUMERIC:
            case DECIMAL:
                ret = "(" +precision+","+scale+")";
                break;
            case VARCHAR:
                ret = "(" +precision+")";
                break;
        }
        return ret;
    }
    public boolean isNullable() {
        return nullable;
    }

    public DbColumn setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public DbColumn setName(String name) {
        this.name = name;
        return this;
    }

    public JDBCType getJdbcType() {
        return jdbcType;
    }

    public DbColumn setJdbcType(JDBCType jdbcType) {
        this.jdbcType = jdbcType;
        return this;
    }

    public int getPrecision() {
        return precision;
    }

    public DbColumn setPrecision(int precision) {
        this.precision = precision;
        return this;
    }

    public int getScale() {
        return scale;
    }

    public DbColumn setScale(int scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public DbTable getParent() {
        return parent;
    }

    @Override
    public void setParent(IContainer parent) {
        this.parent = (DbTable) parent;
    }
}
