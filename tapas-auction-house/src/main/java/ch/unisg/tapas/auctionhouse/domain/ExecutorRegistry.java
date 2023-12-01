package ch.unisg.tapas.auctionhouse.domain;

import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Registry that keeps a track of executors internal to the TAPAS application and the types of tasks
 * they can achieve. One executor may correspond to multiple task types. This mapping is used when
 * bidding for tasks: the auction house will only bid for tasks for which there is a known executor.
 * This class is a singleton.
 */
public class ExecutorRegistry {
    private static final Logger LOGGER = LogManager.getLogger(ExecutorRegistry.class);
    private static ExecutorRegistry registry;

    private final Map<Auction.AuctionedTaskType, Set<ExecutorIdentifier>> executors;

    private ExecutorRegistry() {this.executors = new Hashtable<>();}

    public static synchronized ExecutorRegistry getInstance() {
        if (registry == null) {
            registry = new ExecutorRegistry();
        }

        return registry;
    }

    /**
     * Adds an executor to the registry for a given task type.
     *
     * @param taskType the type of the task
     * @param executorIdentifier the identifier of the executor (can be any string)
     * @return true unless a runtime exception occurs
     */
    public boolean addExecutor(Auction.AuctionedTaskType taskType, ExecutorIdentifier executorIdentifier) {
        Set<ExecutorIdentifier> taskTypeExecs = executors.getOrDefault(taskType,
            Collections.synchronizedSet(new HashSet<>()));

        taskTypeExecs.add(executorIdentifier);
        executors.put(taskType, taskTypeExecs);

        LOGGER.info("Added executor " + executorIdentifier.getValue() + " for task type "
            + taskType.getValue());

        return true;
    }

    /**
     * Removes an executor from the registry. The executor is disassociated from all known task types.
     *
     * @param executorIdentifier the identifier of the executor (can be any string)
     * @return true unless a runtime exception occurs
     */
    public boolean removeExecutor(ExecutorIdentifier executorIdentifier) {
        Iterator<Auction.AuctionedTaskType> iterator = executors.keySet().iterator();

        while (iterator.hasNext()) {
            Auction.AuctionedTaskType taskType = iterator.next();
            Set<ExecutorIdentifier> set = executors.get(taskType);

            set.remove(executorIdentifier);

            if (set.isEmpty()) {
                iterator.remove();
            }
        }

        return true;
    }

    /**
     * Checks if the registry contains an executor for a given task type. Used during an auction to
     * decide if a bid should be placed.
     *
     * @param taskType the task type being auctioned
     * @return true if there is a mapping for the given task type
     */
    public boolean containsTaskType(Auction.AuctionedTaskType taskType) {
        return executors.containsKey(taskType);
    }

    // Value Object for the executor identifier
    @Value
    public static class ExecutorIdentifier {
        String value;
    }
}
