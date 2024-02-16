package ch.unisg.tapaschatgpt;

import ch.unisg.tapaschatgpt.domain.Task;
import ch.unisg.tapaschatgpt.services.ExecutionUseCase;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.Logger;

@Service
public class ChatGPTExecutor implements ExecutionUseCase {
    private static final Logger LOGGER = LogManager.getLogger(ChatGPTExecutor.class);
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "";
    private static final String MODEL = "gpt-3.5-turbo";

    @Override
    public CompletableFuture<Task> executeTask(final Task task) {
        return CompletableFuture.supplyAsync(() -> {
            String inputPrompt = task.getInput();
            if (inputPrompt == null || inputPrompt.isEmpty()) {
                LOGGER.error("Input prompt is null or empty");
                return task;
            }
            try {
                String response = callExternalService(inputPrompt);
                task.setOutput(response);
                LOGGER.info("Task completed successfully");
            } catch (Exception e) {
                LOGGER.error("Error executing task", e);
                task.setOutput("Error: " + e.getMessage());
            }
            return task;
        });
    }

    public String callExternalService(String prompt){
        try{
            URL obj = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");

            // Create the Request Body
            String body = "{\"model\": \"" + MODEL + "\", \"messages\": [{\"role\": \"user\", \"content\": \"You are an unhelpful, sassy coding assistant. I will ask you a question, please provide an unhelpful answer. The intent is to be funny. \\n" + prompt + "\"}]}";
            LOGGER.info(body);

            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // Extract the message from the response
            String extractedResponse = extractMessageFromJSONResponse(response.toString());
            LOGGER.info(extractedResponse);
            return extractedResponse;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred: " + e.getMessage(), e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }
}
