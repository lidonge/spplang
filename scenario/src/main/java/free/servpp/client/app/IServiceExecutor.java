package free.servpp.client.app;

import free.servpp.service.AtomicService;
import free.servpp.service.Role;
import free.servpp.service.scenario.AtomicExecuteException;

/**
 * @author lidong@date 2023-11-08@version 1.0
 */
public interface IServiceExecutor {
    AtomicService getService();
    ILocalServiceContainer getLocalServiceContainer();
    IAppMessageBuilder getAppMessageBuilder();
    IClient getClient();

    default boolean isLocalService(){
        return getLocalServiceContainer().contains(getService());
    }

    default boolean isAsynService(){
        return getAppMessageBuilder().isAsynService();
    }

    /**
     * If client is null, the service is local service.
     *
     * @throws AtomicExecuteException
     */
    default void executeSync() throws AtomicExecuteException {
        if(isLocalService()) {
            ILocalServiceContainer localServiceContainer = getLocalServiceContainer();
            localServiceContainer.execute(getService());
        }else {
            IClient client = getClient();
            IAppMessageBuilder appMessageBuilder = getAppMessageBuilder();
            client.callSyncService(appMessageBuilder.build(getService()));
        }
    }

    default void executeAsync(ICallbackAggregation callbackAggregation) throws AtomicExecuteException {
        IClient client = getClient();
        IAppMessageBuilder appMessageBuilder = getAppMessageBuilder();

        AtomicService service = getService();
        IAppMessage appMessage = appMessageBuilder.build(service);
        client.callAsyncService(appMessage, new ICallbackListener() {
            @Override
            public void serviceCallback(Object role) {
                try {
                    service.copyFrom(role);
                }catch(Throwable ex){
                    callbackAggregation.asyncServiceFinishied(IServiceExecutor.this, ex);
                    return;
                }
                callbackAggregation.asyncServiceFinishied(IServiceExecutor.this, null);
            }
        });
    }
}
