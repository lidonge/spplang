package free.servpp.service.server;

import free.servpp.service.Service;

/**
 * @author lidong@date 2023-11-16@version 1.0
 */
public interface IServiceContainer {
    Service getService(String name);
}
