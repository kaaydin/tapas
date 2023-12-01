package ch.unisg;

import ch.unisg.tapasroster.roster.adapter.out.persistence.mongodb.TaskAssignmentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = TaskAssignmentRepository.class)
public class TapasRosterApplication {

	public static void main(String[] args) {
		SpringApplication tapasRosterApp = new SpringApplication(TapasRosterApplication.class);
		tapasRosterApp.run(args);

	}

}
