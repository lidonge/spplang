package free.servpp.generator.general;

import java.util.Arrays;

/**
 * @author lidong@date 2023-11-17@version 1.0
 */
public class NameUtil {
    public static String firstToLowerCase(String ptype, boolean toLower) {
        String name;
        char first = ptype.charAt(0);
        first = toLower ? Character.toLowerCase(first) : Character.toUpperCase(first);
        StringBuffer sb = new StringBuffer();
        name = sb.append(first).append(ptype.substring(1)).toString();
        return name;
    }

    public static String getNameCall(String name){
        return getNameCall(name,false);
    }
    public static String getNameCall(String name, boolean isSet){
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
                if(count !=0 ) {
                    if(isSet){
                        ret += "set" + NameUtil.firstToLowerCase(rest, false);
                    }else {
                        ret += "get" + NameUtil.firstToLowerCase(rest, false) + "()";
                    }
                }
                break;
            }else{
                if(count !=0 ) {
                    if(isSet){
                        ret += "set" + NameUtil.firstToLowerCase(rest.substring(0, idx), false);
                    }else {
                        ret += "get" + NameUtil.firstToLowerCase(rest.substring(0, idx), false) + "()";
                    }
                }
                else
                    ret = rest.substring(0,idx);
                rest = rest.substring(idx+1);
            }
            count++;
        }
//        System.out.println(ret);
        return ret;
    }
}
