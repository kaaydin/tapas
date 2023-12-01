package ch.unisg.tapasroster.roster.adapter.out.persistence.mongodb;

import ch.unisg.tapasroster.roster.application.port.out.AddTaskAssignmentPort;
import ch.unisg.tapasroster.roster.application.port.out.LoadTaskAssignmentsPort;
import ch.unisg.tapasroster.roster.domain.TaskAssignment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskAssignmentPersistenceAdapter implements AddTaskAssignmentPort, LoadTaskAssignmentsPort {

    @Autowired
    private final TaskAssignmentRepository repository;

    private final TaskAssignmentMapper mapper;

    @Override
    public void addTaskAssignment(TaskAssignment taskAssignment) {
        MongoTaskAssignmentDocument mongoTaskAssignmentDocument = mapper.mapToMongoDocument(taskAssignment);
        repository.save(mongoTaskAssignmentDocument);
        System.out.println(mongoTaskAssignmentDocument);
    }

    @Override
    public List<TaskAssignment> loadTaskAssignments() {
        List<MongoTaskAssignmentDocument> documentList = repository.findAll();
        List<TaskAssignment> taskAssignments = mapper.mapToDomainEntities(documentList);
        return taskAssignments;
    }

}
