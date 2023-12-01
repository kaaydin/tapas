package ch.unisg.tapaschatgpt.controller;

import ch.unisg.tapaschatgpt.domain.Task;
import ch.unisg.tapaschatgpt.services.ExecutorService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class ExecuteTaskController {

    @Autowired
    ExecutorService executionService;

    @PostMapping(value = "/execute")
    public void process(@RequestBody Task task) {
        System.out.println("New task:" + task.getTaskLocation());

        // Retrieve current task location
        String taskLocation = task.getTaskLocation();

        // Retrieve task input
        task.setInput(getTaskInput(taskLocation));

        // Retrieve originalTaskURI
        task.setOriginalTaskUri(getOriginalTaskUri(taskLocation));

        // Print all statements for debugging
        System.out.println("Task Location: " + task.getTaskLocation());
        System.out.println("Original Task Uri: " + task.getOriginalTaskUri());
        System.out.println("Input Data: " + task.getInput());

        executionService.executeTask(task);
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
            System.out.println("Failed to get the Object on URL: " + taskLocation);
            System.out.println(e.getMessage());
            throw e;
        }

        // Retrieve input data
        String inputData = new JSONObject(result).getString("inputData");
        System.out.println("Input Data: " + inputData);

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
            System.out.println("Failed to get the Object on URL: " + taskLocation);
            System.out.println(e.getMessage());
            throw e;}

        // Parse the result string to JSON object
        JSONObject jsonObject = new JSONObject(result);

        // Check if the JSON object contains the key 'originalTaskUri'
        if (jsonObject.has("originalTaskUri")) {
            String originalTaskUri = jsonObject.getString("originalTaskUri");
            System.out.println("Original Task Uri: " + originalTaskUri);
            return originalTaskUri;
        } else {
            // Handle the case where 'originalTaskUri' does not exist
            System.out.println("The key 'originalTaskUri' does not exist in the response.");
            return null; // or any other default or error handling mechanism
        }
    }

}
