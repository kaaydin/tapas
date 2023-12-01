package ch.unisg.tapasexecutorpool.executorpool;

import ch.unisg.tapasexecutorpool.executorpool.adapter.in.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.*;

import java.net.URI;

import static org.assertj.core.api.BDDAssertions.then;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties="spring.profiles.active:local-mongo")
public class AddNewExecutorToExecutorPoolSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private Environment environment;

    @Test
    void addNewExecutorToExecutorPool() throws JSONException {

        String executorName = "system-integration-test-executor";
        String executorType = "system-integration-test-type";
        String executorBaseUri = "system-integration-test-baseuri";

        ResponseEntity response = whenAddNewExecutorToEmptyPool(executorName, executorType, executorBaseUri);

        URI location = response.getHeaders().getLocation();

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(response.getBody() == null);
        then(location.toString()).isNotEmpty();
        then(location.toString()).contains(environment.getProperty("baseuri"));
        then(ExecutorPool.getTapasExecutorPool().getListOfExecutors().getValue()).hasSize(1);

    }

    private ResponseEntity whenAddNewExecutorToEmptyPool(
        String executorname,
        String executorType,
        String executorBaseUri) throws JSONException {

            ExecutorPool.getTapasExecutorPool().getListOfExecutors().getValue().clear();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", ExecutorJsonRepresentation.MEDIA_TYPE);

            String jsonPayLoad = new JSONObject()
                .put("executorName", executorname)
                .put("executorType", executorType)
                .put("executorBaseUri", executorBaseUri)
                .toString();

            HttpEntity<String> request = new HttpEntity<>(jsonPayLoad,headers);

            return restTemplate.exchange(
                "/executors/",
                HttpMethod.POST,
                request,
                Object.class
            );

    }

}
