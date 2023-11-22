package free.servpp.client.app;

import free.servpp.service.AtomicService;

/**
 * @author lidong@date 2023-11-08@version 1.0
 */
public interface IAppMessageBuilder {
    /**
     * The implementer needs to overload this method to add some application parameters
     * required to call the server side system, such as a unique serial number.
     * @return
     */
    IAppMessage build(AtomicService service);
    boolean isAsynService();

}
