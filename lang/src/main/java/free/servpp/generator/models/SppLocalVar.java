package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppLocalVar extends SppParameter{
    int index;

    public SppLocalVar(SppCompilationUnit type, String name) {
        super(type, name);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "SppLocalVar{" +
                "type=" + type.getName() +
                ", name='" + name + '\'' +
                '}';
    }

}
