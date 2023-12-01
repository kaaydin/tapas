package ch.unisg.tapasroster.roster.application.port.out;

import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;

public interface StartExecutorPort {
    void startExecutor(ExecutorBaseUri executorBaseUri, String taskLocation);
}
