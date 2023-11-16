package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;

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
        int idx = -1;
        String rest = name;
        String ret = name;
        int count = 0;
        while(true){
            idx = rest.indexOf(".");
//            System.out.println("idx:"+idx + " ,rest: " +rest);
            if(count != 0) {
                ret += ".";
            }
            if(idx == -1 ){
                if(count !=0 )
                    ret += "get"+ IConstance.firstToLowerCase(rest,false)+"()";
                break;
            }else{
                if(count !=0 )
                    ret += "get"+ IConstance.firstToLowerCase(rest.substring(0,idx),false)+"()";
                else
                    ret = rest.substring(0,idx);
                rest = rest.substring(idx+1);
            }
            count++;
        }
//        System.out.println(ret);
        return ret;
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
