package free.servpp.generator.models.app;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppField;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class SppFieldDefine {
    SppClass sppClass;
    SppField sppField;

    public SppFieldDefine(SppClass sppClass, SppField sppField) {
        this.sppClass = sppClass;
        this.sppField = sppField;
    }

    public SppClass getSppClass() {
        return sppClass;
    }

    public void setSppClass(SppClass sppClass) {
        this.sppClass = sppClass;
    }

    public SppField getSppField() {
        return sppField;
    }

    public void setSppField(SppField sppField) {
        this.sppField = sppField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SppFieldDefine that = (SppFieldDefine) o;
        return sppClass.equals(that.sppClass) && sppField.equals(that.sppField);
    }

    @Override
    public String toString() {
        return "SppFieldDefine{" +
                "sppClass=" + sppClass.getName() +
                ", sppField=" + sppField +
                '}';
    }
}
