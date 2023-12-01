package ch.unisg.tapastasks;

import ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = TaskRepository.class)
public class TapasTasksApplication {

	public static void main(String[] args) {
		SpringApplication tapasTasksApp = new SpringApplication(TapasTasksApplication.class);
		tapasTasksApp.run(args);

	}

}
