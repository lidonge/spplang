package free.servpp.service.scenario;

import free.servpp.client.app.IAppScenario;

/**
 * @author lidong@date 2023-11-03@version 1.0
 */
public interface IApplicationHandler {

    void execScenario(IAppScenario scenario);

    void rollbackScenario(IAppScenario scenario);
}
