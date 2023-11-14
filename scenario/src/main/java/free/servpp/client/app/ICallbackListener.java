package free.servpp.client.app;


import free.servpp.client.Role;

/**
 * @author lidong@date 2023-11-07@version 1.0
 */
public interface ICallbackListener{
    void serviceCallback(Role[] roles);
}
