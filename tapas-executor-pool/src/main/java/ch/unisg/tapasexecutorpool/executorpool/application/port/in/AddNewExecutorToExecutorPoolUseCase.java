package ch.unisg.tapasexecutorpool.executorpool.application.port.in;

public interface AddNewExecutorToExecutorPoolUseCase {
    String addNewExecutorToExecutorPool(AddNewExecutorToExecutorPoolCommand command);
}