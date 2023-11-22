package free.servpp.generator.models.app;

import free.servpp.generator.models.SppClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class AppHeader implements INamedObject{
    private String name;
    private boolean isTemp;
    private List<AppHeader> extendsHeaders = new ArrayList<>();
    private List<SppExtendField> sppExtendFields = new ArrayList<>();

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    public String addExtendsHeader(AppHeader appHeader){
        return appHeader.addToList(extendsHeaders,appHeader);
    }

    public void _addExtendField(SppExtendField field){
        sppExtendFields.add(field);
    }
    public String addExtendField(SppExtendField field){
        return field.addToList(sppExtendFields,field);
    }
    public List<AppHeader> getExtendsHeaders() {
        return extendsHeaders;
    }

    public List<SppExtendField> getSppExtendFields() {
        return sppExtendFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AppHeader{" +
                "name='" + name + '\'' +
                ", isTemp=" + isTemp +
                ", extendsHeaders=" + extToString() +
                ", sppExtendFields=" + sppExtendFields +
                '}';
    }
    private String extToString(){
        String ret = "";
        for(AppHeader header:extendsHeaders){
            ret += header.name+",";
        }
        return ret;
    }
}
