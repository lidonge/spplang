package free.servpp.client.app;

/**
 * @author lidong@date 2023-11-08@version 1.0
 * The application layer client, realized the service call to the server-side. It should convert roles to
 * the parameters of the service api called. The conversion rules may be defined by default rules, or by
 * a mapping file.
 *
 * When the call returns synchronized, maps the return message to the incoming roles.
 * If the service is an asynchronous service, it will call back via callback service.
 * */
public interface IClient {
    void callSyncService(IAppMessage callPackage);

    void callAsyncService(IAppMessage callPackage, ICallbackListener callbackListener);
}
