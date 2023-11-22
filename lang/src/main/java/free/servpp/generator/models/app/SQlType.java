package free.servpp.generator.models.app;

import free.servpp.generator.general.IConstance;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class SQlType {
    String type;
    boolean notnull;
    int precision;
    int scale;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNotnull() {
        return notnull;
    }

    public void setNotnull(boolean notnull) {
        this.notnull = notnull;
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

    @Override
    public String toString() {
        return "SQlType{" +
                "type='" + type + '\'' +
                ", notnull=" + notnull +
                ", precision=" + precision +
                ", scale=" + scale +
                '}';
    }
}
