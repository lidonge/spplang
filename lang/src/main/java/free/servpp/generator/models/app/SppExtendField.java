package free.servpp.generator.models.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppCompilationUnit;
import free.servpp.generator.models.SppField;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class SppExtendField extends SppField implements INamedObject {
    boolean isNotNull;
    boolean isUnique;
    String overrideSuper;

    String defaultValue;

    protected SppExtendField(SppCompilationUnit type, String name, int arrayDimension, int index, boolean isQuantum,
                          boolean isNotNull, boolean isUnique, String overrideSuper, String defaultValue) {
        super(type, name, arrayDimension, index, isQuantum);
        this.isNotNull = isNotNull;
        this.isUnique = isUnique;
        this.overrideSuper = overrideSuper;
        this.defaultValue = defaultValue;
    }

    public SppExtendField(SppCompilationUnit type, String name, int index, boolean isQuantum, boolean isNotNull, boolean isUnique, String overrideSuper, String defaultValue) {
        super(type, name, index, isQuantum);
        this.isNotNull = isNotNull;
        this.isUnique = isUnique;
        this.overrideSuper = overrideSuper;
        this.defaultValue = defaultValue;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new SppExtendField(getType(),getName(),getArrayDimension(),getIndex(),isQuantum(),isNotNull,isUnique,overrideSuper,defaultValue);
    }

    public SppExtendField(SppCompilationUnit cls, String name, boolean isNotNull, boolean isUnique, String overrideSuper, String defaultValue) {
        super(cls, name);
        this.isNotNull = isNotNull;
        this.isUnique = isUnique;
        this.overrideSuper = overrideSuper;
        this.defaultValue = defaultValue;
    }

    public SppExtendField(SppClass cls, String name) {
        super(cls, name);
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public String getOverrideSuper() {
        return overrideSuper;
    }

    public void setOverrideSuper(String overrideSuper) {
        this.overrideSuper = overrideSuper;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "SppExtendField{" +
                "name="+getName()+
                ", isNotNull=" + isNotNull +
                ", isUnique=" + isUnique +
                ", overrideSuper='" + overrideSuper + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}
