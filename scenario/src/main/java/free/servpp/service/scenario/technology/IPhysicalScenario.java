package free.servpp.service.scenario.technology;

import free.servpp.client.AtomicService;
import free.servpp.client.app.*;
import free.servpp.service.scenario.application.*;

import java.util.List;

/**
 * @author lidong@date 2023-11-04@version 1.0
 */
public interface IPhysicalScenario {
    List<IServiceExecutor> getParallelServices();

    ITransactionExecutor getTransactionExecutor();

    ITransactionBlock getTransactionBlock();

    ILocalServiceContainer getLocalServiceContainer();

    IClient getClient(AtomicService service);

    IAppMessageBuilder getAppMessageBuilder(AtomicService service);

    ICallbackAggregation getCallbackAggregation();
}
