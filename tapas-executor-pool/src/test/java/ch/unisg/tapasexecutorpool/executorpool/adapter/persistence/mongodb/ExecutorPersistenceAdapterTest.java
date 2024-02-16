package ch.unisg.tapasexecutorpool.executorpool.adapter.persistence.mongodb;

import ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb.ExecutorPersistenceAdapter;
import ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb.ExecutorRepository;
import ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb.MongoExecutorDocument;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureDataMongo
@Import({ExecutorPersistenceAdapter.class})
public class ExecutorPersistenceAdapterTest {

    @MockBean // Mock the repository
    ExecutorRepository executorRepository;

    @Autowired
    private ExecutorPersistenceAdapter adapterUnderTest;

    @Test
    void addsNewExecutor() {

        String testExecutorId = UUID.randomUUID().toString();
        String testExecutorName = "test-name";
        String testExecutorType = "GREETING";
        String testExecutorBaseUri = "test-base-uri";
        String testExecutorPoolName = "reads-persistence-executor-poolname";

        Executor testExecutor = new Executor(
                testExecutorName,
                testExecutorType,
                testExecutorBaseUri
        );
        
        // Create a mock document to be returned by the repository
        MongoExecutorDocument mockDocument = new MongoExecutorDocument(testExecutorId, testExecutorName, testExecutorType, testExecutorBaseUri, testExecutorPoolName);

        // Stub the findByExecutorId method
        when(executorRepository.findByExecutorId(eq(testExecutorId))).thenReturn(mockDocument);

        adapterUnderTest.addExecutor(testExecutor);

        MongoExecutorDocument retrievedDoc = executorRepository.findByExecutorId(testExecutorId);

        assertThat(retrievedDoc.getExecutorId()).isEqualTo(testExecutorId);
        assertThat(retrievedDoc.getExecutorName()).isEqualTo(testExecutorName);
        assertThat(retrievedDoc.getExecutorType()).isEqualTo(testExecutorType);
        assertThat(retrievedDoc.getExecutorBaseUri()).isEqualTo(testExecutorBaseUri);
    }

    @Test
    void retrievesExecutor() throws ExecutorNotFoundError {

        String testExecutorId = UUID.randomUUID().toString();
        String testExecutorName = "reads-persistence-executor-name";
        String testExecutorType = "reads-persistence-executor-type";
        String testExecutorBaseUri = "reads-persistence-baseuri-type";
        String testExecutorPoolName = "reads-persistence-executor-poolname";

        MongoExecutorDocument mongoExecutor = new MongoExecutorDocument(testExecutorId, testExecutorName, testExecutorType, testExecutorBaseUri, testExecutorPoolName);

        executorRepository.insert(mongoExecutor);

        Executor retrievedExecutor = adapterUnderTest.loadExecutor(testExecutorId);
        assertThat(retrievedExecutor.getExecutorId()).isEqualTo(testExecutorId);
        assertThat(retrievedExecutor.getExecutorName()).isEqualTo(testExecutorName);
        assertThat(retrievedExecutor.getExecutorType()).isEqualTo(testExecutorType);
        assertThat(retrievedExecutor.getExecutorBaseUri()).isEqualTo(testExecutorBaseUri);
    }

}
