package free.servpp.client.app;

import free.servpp.service.scenario.AtomicExecuteException;
import free.servpp.service.scenario.application.IScenarioResult;
import free.servpp.service.scenario.technology.IPhysicalScenario;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author lidong@date 2023-11-03@version 1.0
 */
public interface IAppScenario {
    public static enum Status{
        Success, Error, Unknow
    }

    void finished(IScenarioResult result);

    IScenarioResult createErrorResult(Throwable ex);

    IPhysicalScenario getPhysicalScenario();

    default void execute()
            throws ExecutionException, InterruptedException, TimeoutException, AtomicExecuteException {
        IPhysicalScenario physicalScenario = getPhysicalScenario();
        List<IServiceExecutor> serviceExecutors = physicalScenario.getParallelServices();
        for(IServiceExecutor executor : serviceExecutors) {
            if(executor.isLocalService() || !executor.isAsynService()) {
                execLocalOrSynService(executor);
            } else {
                executor.executeAsync(physicalScenario.getCallbackAggregation());
            }
        }
//        physicalScenario.getTransactionBlock().execute();
    }

    private void execLocalOrSynService(IServiceExecutor executor) {
        CompletableFuture.runAsync(()->{
            try {
                executor.executeSync();
            } catch (AtomicExecuteException e) {
                throw new CompletionException(e);
            }
        }).whenComplete((res,ex)->{
            syncServiceFinished(executor,ex);
        });
    }

    private void syncServiceFinished(IServiceExecutor executor, Throwable ex){
        if(ex != null) {
            finished(createErrorResult(ex));
        }else {
            IPhysicalScenario physicalScenario = getPhysicalScenario();
            ICallbackAggregation callbackAggregation = physicalScenario.getCallbackAggregation();
            callbackAggregation.syncServiceFinishied(executor,ex);
        }
    }
}
