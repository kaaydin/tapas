package ch.unisg.tapasexecutorpool.executorpool.adapter.in.web;

import ch.unisg.tapasexecutorpool.executorpool.adapter.in.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.AddNewExecutorToExecutorPoolCommand;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.AddNewExecutorToExecutorPoolUseCase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

/**
 * Controller that handles HTTP requests for creating new executors.
 */
@RestController
@RequiredArgsConstructor
public class AddNewExecutorToExecutorPoolWebController {

    private final AddNewExecutorToExecutorPoolUseCase addNewExecutorToExecutorPoolUseCase;

    @Autowired
    private Environment environment;

    @PostMapping(path = "/executors/", consumes = {ExecutorJsonRepresentation.MEDIA_TYPE})
    public ResponseEntity<Void> addNewExecutorExecutorToExecutorPool(@RequestBody ExecutorJsonRepresentation payload) {
        String executorName = payload.getExecutorName();
        String executorType = payload.getExecutorType();
        String executorBaseUri = payload.getExecutorBaseUri();

        AddNewExecutorToExecutorPoolCommand command = new AddNewExecutorToExecutorPoolCommand(executorName, executorType, executorBaseUri);
        try {
            String executorId = addNewExecutorToExecutorPoolUseCase.addNewExecutorToExecutorPool(command);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.LOCATION, environment.getProperty("baseuri")
                    + "executors/" + executorId);

            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
