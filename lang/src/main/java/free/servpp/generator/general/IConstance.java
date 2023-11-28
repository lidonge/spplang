package free.servpp.generator.general;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public interface IConstance {
    public static String SUFFIX = ".java";

    public static String compilationUnitPackage = "free.servpp.client";

    public static enum CompilationUnitType {
        entity, reference, role, contract, atomicservice, scenario, rolemapper,Enum
    }

    static final String[] primaryTypes = new String[]{
            "boolean", "char", "String"
            , "short", "int", "long"
            , "float", "double", "date"};

    public static enum ServiceType {
        query, check, calculate, update, unknown
    }


    public enum ScenarioType {
        Auto, serial, parallel
    }

    public enum TransactionType {
        tcc, saga, db
    }
}
