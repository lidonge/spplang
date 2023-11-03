package free.servpp.generator;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public interface IConstance {
    public static String SUFFIX = ".java";

    public static String compilationUnitPackage = "free.servpp";

    public static enum CompilationUnitType {
        entity, reference, role, actas, atomicservice, scenario
    }

    public static enum ServiceType {
        query, check, calculate, update
    }
}
