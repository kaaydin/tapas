package ch.unisg.tapasexecutorpool;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb.ExecutorRepository;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ExecutorRepository.class)
public class ExecutorPoolApplication {

    public static void main(String[] args) {
        SpringApplication executorPoolApplication = new SpringApplication(ExecutorPoolApplication.class);
        executorPoolApplication.run(args);

    }
}