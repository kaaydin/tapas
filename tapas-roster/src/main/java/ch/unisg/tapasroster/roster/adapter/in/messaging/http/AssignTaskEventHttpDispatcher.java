package ch.unisg.tapasroster.roster.adapter.in.messaging.http;

import ch.unisg.tapasroster.roster.adapter.in.formats.TaskJsonRepresentation;
import ch.unisg.tapasroster.roster.application.port.in.AssignTaskEvent;
import ch.unisg.tapasroster.roster.domain.TaskAssignment;
import ch.unisg.tapasroster.roster.application.port.in.AssignTaskEventHandler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

/**
 * This REST controller handles all incoming events of type TaskToBeAssigned.
 */
@RestController

public class AssignTaskEventHttpDispatcher {

    private final Environment environment;

    private final AssignTaskEventHandler assignTaskEventHandler;

    public AssignTaskEventHttpDispatcher(Environment environment, AssignTaskEventHandler assignTaskEventHandler) {
        this.environment = environment;
        this.assignTaskEventHandler = assignTaskEventHandler;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created New Task Assignment."),
            @ApiResponse(responseCode = "500", description = "Task assignment could not be created."),
    })
    @PostMapping(path = "/roster/newtask/", consumes = {TaskJsonRepresentation.MEDIA_TYPE})
    public TaskAssignment dispatchAssignTaskEvents(@RequestBody @Valid TaskJsonRepresentation payload) {
        AssignTaskEvent event = new AssignTaskEvent(
                payload.getTaskLocation(),
                payload.getTaskListName(),
                payload.getTaskType()
        );

        return assignTaskEventHandler.assignTaskToExecutor(event);

    }
}

