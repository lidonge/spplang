package free.servpp.service;

/**
 * @author lidong@date 2023-10-31@version 1.0
 */
public interface Scenario extends Service{
    String startTransaction();
    void endTransaction(String id);
}
