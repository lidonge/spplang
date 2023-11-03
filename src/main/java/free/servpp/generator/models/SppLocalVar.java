package free.servpp.generator.models;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppLocalVar {
    SppClass type;
    String name;
    int index;

    public SppLocalVar(SppClass cls, String name) {
        this.type = cls;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SppClass getType() {
        return type;
    }

    public void setType(SppClass type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SppLocalVar{" +
                "type=" + type.getName() +
                ", name='" + name + '\'' +
                '}';
    }
}
