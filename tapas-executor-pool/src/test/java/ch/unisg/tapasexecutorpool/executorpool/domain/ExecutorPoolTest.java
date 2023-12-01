package ch.unisg.tapasexecutorpool.executorpool.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import java.io.*;
import java.util.*;

public class ExecutorPoolTest {

    @Test
    void addNewExecutorToExecutorPoolSuccess() {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();
        executorPool.getListOfExecutors().getValue().clear();
        Executor newExecutor = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor",
                "JOKING", "my-test-executor");

        assertThat(newExecutor.getExecutorName()).isEqualTo("My-Test-Executor");
        assertThat(executorPool.getListOfExecutors().getValue()).hasSize(1);
        assertThat(executorPool.getListOfExecutors().getValue().get(0)).isEqualTo(newExecutor);

    }

    @Test
    void retrieveExecutorSuccess() {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();
        Executor newExecutor = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor2",
                "JOKING", "my-test-executor-2");

        Executor retrievedExecutor = null;
        try {
            retrievedExecutor = executorPool.retrieveExecutorById(newExecutor.getExecutorId());
        } catch (ExecutorNotFoundError e) {
            throw new RuntimeException(e);
        }

        assertThat(retrievedExecutor).isEqualTo(newExecutor);

    }

    @Test
    void retrieveExecutorFailure() {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();
        Executor newExecutor = ExecutorPool.getTapasExecutorPool().addNewExecutorWithNameAndTypeAndURI("My-Test-Executor3",
                "JOKING", "my-test-executor-3");

        String fakeId = "fake-id";

        ExecutorNotFoundError thrown = Assertions.assertThrows(ExecutorNotFoundError.class, () -> {
            executorPool.retrieveExecutorById(fakeId);
        });


    }

    @Test
    void retrieveExecutorsByTypeSuccess() {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();

        Executor newExecutor = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor4",
                "CALCULATING", "my-test-executor-4");
        Executor newExecutor2 = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor5",
                "CALCULATING", "my-test-executor-5");
        Executor newExecutor3 = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor6",
                "CALCULATING", "my-test-executor-6");

        assertThat(executorPool.retrieveExecutorsByType("CALCULATING")).hasSize(3);
        assertThat(executorPool.retrieveExecutorsByType("CALCULATING")).contains(newExecutor);
        assertThat(executorPool.retrieveExecutorsByType("CALCULATING")).contains(newExecutor2);
        assertThat(executorPool.retrieveExecutorsByType("CALCULATING")).contains(newExecutor3);
    }

    @Test
    void retrieveExecutorsByTypeFailure() {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();

        Executor newExecutor = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor7",
                "JOKING", "my-test-executor-7");
        Executor newExecutor2 = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor8",
                "JOKING", "my-test-executor-8");
        Executor newExecutor3 = executorPool.addNewExecutorWithNameAndTypeAndURI("My-Test-Executor9",
                "JOKING", "my-test-executor-9");

        assertThat(executorPool.retrieveExecutorsByType("something")).hasSize(0);
    }

}
