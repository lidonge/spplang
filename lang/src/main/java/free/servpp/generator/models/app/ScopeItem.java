package free.servpp.generator.models.app;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.models.SppService;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class ScopeItem {
    SppService service;
    boolean isAsync;
    IConstance.TransactionType transactionType = IConstance.TransactionType.db;

    public SppService getService() {
        return service;
    }

    public void setService(SppService service) {
        this.service = service;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public IConstance.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(IConstance.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "ScopeItem{" +
                "service=" + service +
                ", isAsync=" + isAsync +
                ", transactionType=" + transactionType +
                '}';
    }
}
