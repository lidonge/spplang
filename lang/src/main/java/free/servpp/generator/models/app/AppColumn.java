package free.servpp.generator.models.app;

import free.servpp.generator.models.SppClass;

/**
 * @author lidong@date 2023-12-05@version 1.0
 */
public class AppColumn {
    private boolean isPrimaryKey;
    private boolean nullable =true;

    private SppClass type;
    private int precision;
    private int scale;
    private String name;

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public SppClass getType() {
        return type;
    }

    public void setType(SppClass type) {
        this.type = type;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
