package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppField extends SppLocalVar {
    public SppField(SppClass cls, String name) {
        super(cls, name);
    }

    public String getUpperName(){
        return IConstance.firstToLowerCase(getName(),false);
    }
}
