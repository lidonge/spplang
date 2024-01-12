package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class SppLocalVar extends SppParameter{
    int index;

    boolean isQuantum;

    protected SppLocalVar(SppCompilationUnit type, String name, int arrayDimension, int index, boolean isQuantum) {
        super(type, name, arrayDimension);
        this.index = index;
        this.isQuantum = isQuantum;
    }

    public SppLocalVar(SppCompilationUnit type, String name, int index, boolean isQuantum) {
        super(type, name);
        this.index = index;
        this.isQuantum = isQuantum;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new SppLocalVar(type,name,getArrayDimension(),index,isQuantum);
    }

    public SppLocalVar(SppCompilationUnit type, String name) {
        super(type, name);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isQuantum() {
        return isQuantum;
    }

    public void setQuantum(boolean quantum) {
        isQuantum = quantum;
    }

    @Override
    public String toString() {
        return "SppLocalVar{" +
                "type=" + type.getName() +
                ", name='" + name + '\'' +
                '}';
    }

}
