package ch.unisg.tapasexecutorpool.executorpool.adapter.in.web;

import ch.unisg.tapasexecutorpool.executorpool.adapter.in.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorFromExecutorPoolQuery;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorFromExecutorPoolUseCase;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RetrieveExecutorFromExecutorPoolWebController {

    private final RetrieveExecutorFromExecutorPoolUseCase retrieveExecutorFromExecutorPoolUseCase;

    public RetrieveExecutorFromExecutorPoolWebController(RetrieveExecutorFromExecutorPoolUseCase retrieveExecutorFromExecutorPoolUseCase) {
        this.retrieveExecutorFromExecutorPoolUseCase = retrieveExecutorFromExecutorPoolUseCase;
    }

    @GetMapping(path = "/executors/{executorId}")
    public ResponseEntity<String> retrieveExecutorFromExecutorPool(@PathVariable("executorId") String executorId) {
        RetrieveExecutorFromExecutorPoolQuery query = new RetrieveExecutorFromExecutorPoolQuery(executorId);
        try {
            Executor retrievedExecutor = retrieveExecutorFromExecutorPoolUseCase.retrieveExecutorFromExecutorPool(query);
            String executorRepresentation = ExecutorJsonRepresentation.serialize(retrievedExecutor);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, ExecutorJsonRepresentation.MEDIA_TYPE);
            return new ResponseEntity<>(executorRepresentation, responseHeaders, HttpStatus.OK);
        } catch (ExecutorNotFoundError e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}