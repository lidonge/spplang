package free.servpp;

/**
 * @author lidong@date 2023-10-31@version 1.0
 */
public interface Scenario {
    default String startTransaction(){
        return "";
    }
    default void endTransaction(String id){

    }
}
