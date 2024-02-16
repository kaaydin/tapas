package ch.unisg.tapasexecutorpool.executorpool.domain;

import lombok.Getter;
import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**This is our aggregate root**/
public class ExecutorPool {
    private static final Logger LOGGER = LogManager.getLogger(ExecutorPool.class);

    // Defining all Getters / Setters
    @Getter
    private final ExecutorPoolName executorPoolName;

    @Getter
    private final ListOfExecutors listOfExecutors;

    // Defining all Value objects
    @Value
    public static class ExecutorPoolName {
        private String value;
    }

    @Value
    public static class ListOfExecutors {
        private List<Executor> value;
    }

    // Construction of the pool
    private static final ExecutorPool executorPool = new ExecutorPool(new ExecutorPoolName("executor-pool-group5"));

    private ExecutorPool(ExecutorPoolName executorPoolName) {
        this.executorPoolName = executorPoolName;
        this.listOfExecutors = new ListOfExecutors(new LinkedList<Executor>());
    }

    public static ExecutorPool getTapasExecutorPool() {
        return executorPool;
    }

    public Executor addNewExecutorWithNameAndTypeAndURI(String executorName, String executorType, String executorBaseUri) {
        Executor newExecutor = Executor.createExecutorWithNameAndTypeAndBaseUri(executorName, executorType, executorBaseUri);
        addExecutorToExecutorPool(newExecutor);

        return newExecutor;
    }

    private void addExecutorToExecutorPool(Executor newExecutor) {
        listOfExecutors.value.add(newExecutor);
        LOGGER.info("Number of Executors: " + listOfExecutors.value.size());
    }

    public Executor retrieveExecutorById(String id) throws ExecutorNotFoundError {
        for (Executor executor : listOfExecutors.value) {
            if (executor.getExecutorId().equalsIgnoreCase(id)) { // Adjusted for ExecutorId's value
                return executor;
            }
        }
        throw new ExecutorNotFoundError();
    }

    public List<Executor> retrieveExecutorsByType(String executorType) {
        List<Executor> executors = new LinkedList<>();

        for (Executor executor : listOfExecutors.value) {
            if (executor.getExecutorType().equals(executorType)) { // Adjusted for ExecutorId's value
                executors.add(executor);
            }
        }
        return executors;
    }

}
