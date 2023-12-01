package ch.unisg.tapasroster.roster.adapter.out.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskAssignmentRepository extends MongoRepository<MongoTaskAssignmentDocument, String> {


}
