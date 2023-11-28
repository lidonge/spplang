package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppField extends SppLocalVar {
    public SppField(SppCompilationUnit cls, String name) {
        super(cls, name);
    }

    @Override
    public String toString() {
        return "SppField{" +
                "type=" + type.getName() +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        boolean ret = super.equals(obj);
        if(obj instanceof SppField){
            SppField field = (SppField) obj;
            ret |= getType().equals(field.getType()) && getName().equals(field.getName());
        }
        return ret;
    }
}
