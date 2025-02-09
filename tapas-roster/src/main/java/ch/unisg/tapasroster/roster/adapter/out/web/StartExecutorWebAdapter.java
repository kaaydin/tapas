package ch.unisg.tapasroster.roster.adapter.out.web;

import ch.unisg.tapasroster.roster.adapter.in.formats.TaskJsonRepresentation;
import ch.unisg.tapasroster.roster.application.port.out.StartExecutorPort;
import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.logging.Logger;

@Component
public class StartExecutorWebAdapter implements StartExecutorPort {
    private static final Logger LOG = Logger.getLogger(StartExecutorWebAdapter.class.getName());

    private final Environment environment;

    public StartExecutorWebAdapter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String startExecutor(ExecutorBaseUri executorBaseUri, String taskLocation) {

        String body = "{\"taskLocation\":\""+ taskLocation +"\"}";

        return WebClient.create()
                .post()
                .uri(executorBaseUri.getBaseUri() + "execute")
                .contentType(MediaType.valueOf(TaskJsonRepresentation.MEDIA_TYPE))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> LOG.info("Invoked task execution for task at %s" + taskLocation))
                .doOnError(err -> LOG.severe(String.format("Invoking task execution for task at %s failed with the following reason: " + taskLocation, err.getMessage())))
                .block();
    }
}
