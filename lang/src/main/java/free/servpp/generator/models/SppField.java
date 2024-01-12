package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppField extends SppLocalVar {
    private SppClass parent;

    protected SppField(SppCompilationUnit type, String name, int arrayDimension, int index, boolean isQuantum) {
        super(type, name, arrayDimension, index, isQuantum);
    }

    public SppField(SppCompilationUnit type, String name, int index, boolean isQuantum) {
        super(type, name, index, isQuantum);
    }

    public SppField(SppCompilationUnit cls, String name) {
        super(cls, name);
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new SppField(type,name,getArrayDimension(),index,isQuantum);
    }

    public SppClass getParent() {
        return parent;
    }

    public void setParent(SppClass parent) {
        this.parent = parent;
    }

    public String getQualifiedInstName(){
        if(getParent() == null)
            return name;

        return getParent().getQualifiedInstName() + "." + name;
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
