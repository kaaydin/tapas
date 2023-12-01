package ch.unisg.tapasroster.roster.application.port.out;

import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;


public interface FindExecutorPort {
    ExecutorBaseUri findExecutor(String taskType);
}
