package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppParameter {
    SppCompilationUnit type;
    String name;

    private int arrayDimension;

    protected SppParameter(SppCompilationUnit type, String name, int arrayDimension) {
        this.type = type;
        this.name = name;
        this.arrayDimension = arrayDimension;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new SppParameter(type,name,arrayDimension);
    }

    public SppParameter(SppCompilationUnit type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getArrayString(){
        String ret = "";
        for(int i = 0;i<arrayDimension;i++){
            ret +="[]";
        }
        return ret;
    }

    public String getToArrayString(){
        if(arrayDimension != 0){
            return ".toArray(new "+type.getName()+"[arg.get"+getUpperName()+"().size()])";
        }
        return "";
    }

    public String getUpperName(){
        return NameUtil.firstToLowerCase(getName(),false);
    }

    public int getArrayDimension() {
        return arrayDimension;
    }

    public SppParameter setArrayDimension(int arrayDimension) {
        this.arrayDimension = arrayDimension;
        return this;
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

    /**
     * For mustache
     * @return
     */
    public String getNameCall(){
        return NameUtil.getNameCall(name);
    }
    @Override
    public String toString() {
        return "SppParameter{" +
                "type=" + getTypeName() + getArrayString()+
                ", name='" + name + '\'' +
                '}';
    }

    private String getTypeName() {
        return type == null ? null:type.getName();
    }
}
