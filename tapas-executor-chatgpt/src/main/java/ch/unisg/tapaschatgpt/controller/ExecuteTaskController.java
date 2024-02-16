package ch.unisg.tapaschatgpt.controller;

import ch.unisg.tapaschatgpt.domain.Task;
import ch.unisg.tapaschatgpt.services.ExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

@RestController
public class ExecuteTaskController {
    private static final Logger LOGGER = LogManager.getLogger(ExecuteTaskController.class);

    @Autowired
    ExecutorService executionService;

    @PostMapping(value = "/execute")
    public void process(@RequestBody Task task) {
        LOGGER.info("New task:" + task.getTaskLocation());

        // Retrieve current task location
        String taskLocation = task.getTaskLocation();

        // Retrieve task input
        task.setInput(getTaskInput(taskLocation));

        // Retrieve originalTaskURI
        task.setOriginalTaskUri(getOriginalTaskUri(taskLocation));

        // Print all statements for debugging
        LOGGER.info("Task Location: " + task.getTaskLocation());
        LOGGER.info("Original Task Uri: " + task.getOriginalTaskUri());
        LOGGER.info("Input Data: " + task.getInput());

        try {
            executionService.executeTask(task);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTaskInput(String taskLocation) {

        // Instantiate new RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Instantiate result string
        String result = "";


        // Retrieve JSON object
        try {
            result = restTemplate.getForObject(taskLocation,String.class);
        } catch (RestClientException e){
            LOGGER.info("Failed to get the Object on URL: " + taskLocation);
            LOGGER.info(e.getMessage());
            throw e;
        }

        // Retrieve input data
        String inputData = new JSONObject(result).getString("inputData");
        LOGGER.info("Input Data: " + inputData);

        return inputData;
    }

    private String getOriginalTaskUri(String taskLocation) {

        // Instantiate new resttemplate
        RestTemplate restTemplate = new RestTemplate();

        // Instantiate result string
        String result = "";

        // Try to retrieve JSON objection

        try {
            result = restTemplate.getForObject(taskLocation,String.class); }

        catch (RestClientException e){
            LOGGER.info("Failed to get the Object on URL: " + taskLocation);
            LOGGER.info(e.getMessage());
            throw e;}

        // Parse the result string to JSON object
        JSONObject jsonObject = new JSONObject(result);

        // Check if the JSON object contains the key 'originalTaskUri'
        if (jsonObject.has("originalTaskUri")) {
            String originalTaskUri = jsonObject.getString("originalTaskUri");
            LOGGER.info("Original Task Uri: " + originalTaskUri);
            return originalTaskUri;
        } else {
            // Handle the case where 'originalTaskUri' does not exist
            LOGGER.info("The key 'originalTaskUri' does not exist in the response.");
            return null; // or any other default or error handling mechanism
        }
    }

}
