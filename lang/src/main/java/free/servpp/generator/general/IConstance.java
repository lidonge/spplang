package free.servpp.generator.general;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public interface IConstance {
    public static String SUFFIX = ".java";

    public static String compilationUnitPackage = "free.servpp.client";

    public static enum CompilationUnitType {
        entity, reference, role, actas, atomicservice, scenario, rolemapper
    }
    static final String[] primaryTypes = new String[]{
            "boolean", "char", "String"
            , "short", "int", "long"
            , "float", "double", "date"};

    public static enum ServiceType {
        query, check, calculate, update, unknown
    }


    static String firstToLowerCase(String ptype, boolean toLower) {
        String name;
        char first = ptype.charAt(0);
        first = toLower? Character.toLowerCase(first) : Character.toUpperCase(first);
        StringBuffer sb = new StringBuffer();
        name = sb.append(first).append(ptype.substring(1)).toString();
        return name;
    }
}
