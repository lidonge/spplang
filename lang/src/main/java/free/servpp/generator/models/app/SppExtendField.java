package free.servpp.generator.models.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppField;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class SppExtendField extends SppField implements INamedObject {
    boolean isNotNull;
    boolean isUnique;
    String overrideSuper;

    String defaultValue;

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
