package ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb;

import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureDataMongo
@Import({TaskPersistenceAdapter.class, TaskMapper.class})
public class TaskPersistenceAdapterTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskPersistenceAdapter adapterUnderTest;

    @Test
    void addsNewTask() {

        String testTaskId = UUID.randomUUID().toString();
        String testTaskName = "adds-persistence-task-name";
        String testTaskOriginalUri = null;
        String testTaskType = "adds-persistence-task-type";
        String testTaskInput = "bla";
        String testTaskOutput = "blub";
        String testTaskStatus = Task.Status.OPEN.toString();
        String testTaskServiceProvider = null;
        String testTaskListName = "tapas-tasks-tutors";

        Task testTask = new Task(
            new Task.TaskId(testTaskId),
                new Task.TaskName(testTaskName),
                new Task.OriginalTaskUri(testTaskOriginalUri),
                new Task.TaskType(testTaskType),
                new Task.InputData(testTaskInput),
                new Task.OutputData(testTaskOutput),
                new Task.TaskStatus(Task.Status.valueOf(testTaskStatus)),
                new Task.ServiceProvider(testTaskServiceProvider)
            );
        adapterUnderTest.addTask(testTask);

        MongoTaskDocument retrievedDoc = taskRepository.findByTaskId(testTaskId,testTaskListName);

        assertThat(retrievedDoc.taskId).isEqualTo(testTaskId);
        assertThat(retrievedDoc.taskName).isEqualTo(testTaskName);
        assertThat(retrievedDoc.taskListName).isEqualTo(testTaskListName);

    }

    @Test
    void retrievesTask() {

        String testTaskId = UUID.randomUUID().toString();
        String testTaskName = "reads-persistence-task-name";
        String testTaskOriginalUri = null;
        String testTaskType = "reads-persistence-task-type";
        String testTaskInput = null;
        String testTaskOutput = null;
        String testTaskStatus = Task.Status.OPEN.toString();
        String testTaskServiceProvider = null;
        String testTaskListName = "tapas-tasks-tutors";

        MongoTaskDocument mongoTask = new MongoTaskDocument(testTaskId, testTaskName, testTaskOriginalUri,
            testTaskType, testTaskInput, testTaskOutput, testTaskStatus, testTaskServiceProvider, testTaskListName);
        taskRepository.insert(mongoTask);

        try {
        Task retrievedTask = adapterUnderTest.loadTask(new Task.TaskId(testTaskId),
            new TaskList.TaskListName(testTaskListName));
            assertThat(retrievedTask.getTaskName().getValue()).isEqualTo(testTaskName);
            assertThat(retrievedTask.getTaskId().getValue()).isEqualTo(testTaskId);
            assertThat(retrievedTask.getTaskStatus().getValue()).isEqualTo(Task.Status.valueOf(testTaskStatus));
        } catch (TaskNotFoundError e) {
           System.out.println(e);
        }
    }

}
