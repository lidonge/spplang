package free.servpp.client.app;

import free.servpp.service.AtomicService;

/**
 * @author lidong@date 2023-11-08@version 1.0
 */
public interface ILocalServiceContainer {
    boolean contains(AtomicService service);

    void execute(AtomicService service);
}
