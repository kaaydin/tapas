package ch.unisg.tapasexecutorpool.executorpool.adapter.in.web;

import ch.unisg.tapasexecutorpool.executorpool.adapter.in.formats.ExistingExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorsByTypeUseCase;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorsByTypeQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class RetrieveExecutorsByTypeWebController {

    private final RetrieveExecutorsByTypeUseCase retrieveExecutorsByTypeUseCase;

    public RetrieveExecutorsByTypeWebController(RetrieveExecutorsByTypeUseCase retrieveExecutorsByTypeUseCase) {
        this.retrieveExecutorsByTypeUseCase = retrieveExecutorsByTypeUseCase;
    }

    @GetMapping(path = "/executors/type/{executorType}")
    public ResponseEntity<String> retrieveExecutors(@PathVariable("executorType") String executorType) {
        if (executorType == null || executorType.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Executor type is required");
        }
        RetrieveExecutorsByTypeQuery query = new RetrieveExecutorsByTypeQuery(executorType);
        List<String> executors;

        try{
            executors = retrieveExecutorsByTypeUseCase
                    .retrieveExecutorsByType(query)
                    .stream()
                    .map(executor -> {
                        try {
                            return ExistingExecutorJsonRepresentation.serialize(executor);
                        } catch (JsonProcessingException e) {
                            // This will get caught by the outer catch block
                            throw new RuntimeException("JSON processing error", e);
                        }
                    })
                    .toList();
        } catch (RuntimeException e) {
            // If the cause is JsonProcessingException, handle it appropriately
            if (e.getCause() instanceof JsonProcessingException) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing JSON: " + e.getCause().getMessage());
            } else {
                throw e; // Re-throw if it's not a JsonProcessingException
            }
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, ExistingExecutorJsonRepresentation.MEDIA_TYPE);

        return new ResponseEntity<>(executors.toString(), responseHeaders, HttpStatus.OK);
    }
}