package ch.unisg.tapascalculator.service;

import ch.unisg.tapascalculator.CalculatorExecutor;
import ch.unisg.tapascalculator.domain.Task;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CalculatorExecutorTest {


    @Test
    void executingTaskSucceeds() {

        // given
        Task newTask = new Task();
        newTask.setInput("1+1");

        // when
        CalculatorExecutor executionService = new CalculatorExecutor();
        Task executedTask = executionService.executeTask(newTask);

        // then
        // check that newTask gets modified
        assertThat(newTask.getOutput()).isEqualTo("2");
        // check that return task is correct
        assertThat(executedTask.getOutput()).isEqualTo("2");

    }

    // Mockito will temporarily not be used in this test
    /*private Task givenATaskWithInput(String taskInput) {
        Task task = Mockito.mock(Task.class);
        given(task.getInput()).willReturn(taskInput);

        return task;
    }*/
}