package free.servpp.client.app;

import free.servpp.client.AtomicService;
import free.servpp.client.Role;
import free.servpp.service.scenario.AtomicExecuteException;

/**
 * @author lidong@date 2023-11-04@version 1.0
 */
public interface IParallelBlock extends IServiceExecutor {
    IServiceExecutor next();

    boolean hasAsynService();

    @Override
    default boolean isAsynService() {
        return hasAsynService();
    }

    @Override
    default void executeSync() throws AtomicExecuteException {
        IServiceExecutor executor = null;
        while ((executor = next()) != null) {
            executor.executeSync();
        }
    }

    @Override
    default void executeAsync(ICallbackAggregation callbackAggregation) throws AtomicExecuteException {
        executeNext(callbackAggregation);
    }

    private void executeNext(ICallbackAggregation callbackAggregation) throws AtomicExecuteException {
        IServiceExecutor executor = next();
        if (executor == null) {
            //finished
            callbackAggregation.asyncServiceFinishied(this, null);
        } else {
            if (executor.isLocalService() || !executor.isAsynService()) {
                executor.executeSync();
                executeNext(callbackAggregation);
            } else {
                IAppMessageBuilder builder = getAppMessageBuilder();
                AtomicService service = executor.getService();
                IAppMessage appMessage = builder.build(service);
                IClient client = getClient();
                client.callAsyncService(appMessage, new ICallbackListener() {
                    @Override
                    public void serviceCallback(Role[] roles) {
                        service.copyFrom(roles);
                        try {
                            executeNext(callbackAggregation);
                        } catch (AtomicExecuteException e) {
                            callbackAggregation.asyncServiceFinishied(IParallelBlock.this,e);
                        }
                    }
                });
            }
        }
    }
}
