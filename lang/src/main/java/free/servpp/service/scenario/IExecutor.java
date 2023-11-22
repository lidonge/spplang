package free.servpp.service.scenario;

import free.servpp.service.Service;

/**
 * @author lidong@date 2023-11-20@version 1.0
 */
public interface IExecutor {
    Service getService();
    void execute();
}
