package free.servpp.service.scenario.application;

import free.servpp.service.scenario.AtomicExecuteException;

/**
 * @author lidong@date 2023-11-04@version 1.0
 */
public interface IAtomicService {
    void execute(IScenarioContext scenarioContext) throws AtomicExecuteException;

    boolean isAsynService();

    void executeAsyn(IScenarioContext scenarioContext);
}
