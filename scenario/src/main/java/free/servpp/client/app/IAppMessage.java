package free.servpp.client.app;

import free.servpp.client.Role;

/**
 * @author lidong@date 2023-11-08@version 1.0
 */
public interface IAppMessage {
    void setRoles(Role[] roles);
    Role[] getRoles();

    IAppHeaders getAppHeaders();
}
