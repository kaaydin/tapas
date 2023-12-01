package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;

public interface RetrieveTaskFromTaskListUseCase {
    Task retrieveTaskFromTaskList(RetrieveTaskFromTaskListQuery query) throws TaskNotFoundError;
}
