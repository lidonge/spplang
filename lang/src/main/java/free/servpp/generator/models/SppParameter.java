package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppParameter {
    SppClass type;
    String name;

    public SppParameter(SppClass type, String name) {
        this.type = type;
        this.name = name;
    }

    public SppClass getType() {
        return type;
    }

    public void setType(SppClass type) {
        this.type = type;
    }

    public String  getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCall(){
        return NameUtil.getNameCall(name);
    }
    @Override
    public String toString() {
        return "SppParameter{" +
                "type=" + getTypeName() +
                ", name='" + name + '\'' +
                '}';
    }

    private String getTypeName() {
        return type == null ? null:type.getName();
    }
}
