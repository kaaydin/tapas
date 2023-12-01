package ch.unisg.tapasexecutorpool.executorpool.application.service;

import ch.unisg.tapasexecutorpool.executorpool.application.port.in.AddNewExecutorToExecutorPoolCommand;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.AddNewExecutorToExecutorPoolUseCase;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.AddExecutorPort;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("AddNewExecutorToPool")
public class AddNewExecutorToExecutorPoolService implements AddNewExecutorToExecutorPoolUseCase {

    private final AddExecutorPort addExecutorToRepositoryPort;
    private final LoadExecutorPort loadExecutorPort;

    @Override
    public String addNewExecutorToExecutorPool(AddNewExecutorToExecutorPoolCommand command) {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();
        Executor newExecutor;

        newExecutor = executorPool.addNewExecutorWithNameAndTypeAndURI(
                command.getExecutorName(),
                command.getExecutorType(),
                command.getExecutorBaseUri());

        addExecutorToRepositoryPort.addExecutor(newExecutor);

        return newExecutor.getExecutorId();
    }
}