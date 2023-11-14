package free.servpp.client.app;

/**
 * @author lidong@date 2023-11-08@version 1.0
 */
public interface ICallbackAggregation {
    void syncServiceFinishied(IServiceExecutor executor, Throwable ex);

    void asyncServiceFinishied(IServiceExecutor iServiceExecutor, Throwable ex);
}
