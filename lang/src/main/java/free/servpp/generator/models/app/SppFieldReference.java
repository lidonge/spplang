package free.servpp.generator.models.app;

import free.servpp.generator.models.SppField;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class SppFieldReference {
//    private SppClass sppClass;
    private SppField sppField;

    public SppFieldReference(SppField sppField) {
//        this.sppClass = sppClass;
        this.sppField = sppField;
    }

//    public SppClass getSppClass() {
//        return sppClass;
//    }
//
//    public void setSppClass(SppClass sppClass) {
//        this.sppClass = sppClass;
//    }

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
        SppFieldReference that = (SppFieldReference) o;
        return sppField.getParent().equals(that.getSppField().getParent()) && sppField.equals(that.sppField);
    }

    @Override
    public String toString() {
        return "SppFieldDefine{" +
                "sppClass=" + sppField.getParent().getName() +
                ", sppField=" + sppField +
                '}';
    }
}
