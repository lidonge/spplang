package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppLocalVar {
    SppCompilationUnit type;
    String name;
    int index;

    public SppLocalVar(SppCompilationUnit cls, String name) {
        this.type = cls;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SppCompilationUnit getType() {
        return type;
    }

    public void setType(SppCompilationUnit type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpperName(){
        return NameUtil.firstToLowerCase(getName(),false);
    }

    @Override
    public String toString() {
        return "SppLocalVar{" +
                "type=" + type.getName() +
                ", name='" + name + '\'' +
                '}';
    }
}
