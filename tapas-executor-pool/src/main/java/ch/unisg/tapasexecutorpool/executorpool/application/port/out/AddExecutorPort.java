package ch.unisg.tapasexecutorpool.executorpool.application.port.out;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;

public interface AddExecutorPort {

    void addExecutor(Executor executor);

}
