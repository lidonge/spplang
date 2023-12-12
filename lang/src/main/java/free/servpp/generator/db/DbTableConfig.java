package free.servpp.generator.db;

import java.sql.JDBCType;

/**
 * @author lidong@date 2023-12-07@version 1.0
 */
public class DbTableConfig {
    public enum NamingType{
        snake_case, camelCase, PascalCase, UPPERCASE, lowercase
    }
    private int varchar_precision = 50;
    private int decimal_precision = 17;

    private int decimal_scale = 2;
    private int bigint_precision = 20;
    private boolean nullable = true;
    private java.sql.JDBCType type_boolean = JDBCType.BOOLEAN;
    private java.sql.JDBCType type_String = JDBCType.VARCHAR;

    public int getDecimal_scale() {
        return decimal_scale;
    }

    public DbTableConfig setDecimal_scale(int decimal_scale) {
        this.decimal_scale = decimal_scale;
        return this;
    }

    public int getBigint_precision() {
        return bigint_precision;
    }

    public DbTableConfig setBigint_precision(int bigint_precision) {
        this.bigint_precision = bigint_precision;
        return this;
    }

    public int getVarchar_precision() {
        return varchar_precision;
    }

    public void setVarchar_precision(int varchar_precision) {
        this.varchar_precision = varchar_precision;
    }

    public int getDecimal_precision() {
        return decimal_precision;
    }

    public void setDecimal_precision(int decimal_precision) {
        this.decimal_precision = decimal_precision;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public JDBCType getType_boolean() {
        return type_boolean;
    }

    public void setType_boolean(JDBCType type_boolean) {
        this.type_boolean = type_boolean;
    }

    public JDBCType getType_String() {
        return type_String;
    }

    public void setType_String(JDBCType type_String) {
        this.type_String = type_String;
    }
}
