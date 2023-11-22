package free.servpp.generator.models;

import java.util.List;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class TransactionBlock extends ServiceBaseBody implements IServiceBodyStatement {
    public List<IServiceBodyStatement> getTransactionCall() {
        return getSppServiceCallList();
    }
}
