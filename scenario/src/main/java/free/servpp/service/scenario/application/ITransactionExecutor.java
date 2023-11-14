package free.servpp.service.scenario.application;

import free.servpp.client.app.ITransactionBlock;

/**
 * @author lidong@date 2023-11-06@version 1.0
 */
public interface ITransactionExecutor {
    void execute(ITransactionBlock transactionBlock);
}
