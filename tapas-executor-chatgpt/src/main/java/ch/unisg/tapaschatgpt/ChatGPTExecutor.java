package ch.unisg.tapaschatgpt;

import ch.unisg.tapaschatgpt.domain.Task;
import ch.unisg.tapaschatgpt.services.ExecutionUseCase;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Service
public class ChatGPTExecutor implements ExecutionUseCase {

    @Override
    public Task executeTask(final Task task) {

            class ChatGPTAPIExample {

                public static String chatGPT(String prompt) {
                    String url = "https://api.openai.com/v1/chat/completions";
                    String apiKey = "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";
                    String model = "gpt-3.5-turbo";

                    try {
                        URL obj = new URL(url);
                        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                        connection.setRequestProperty("Content-Type", "application/json");

                        // Create the Request Body
                        String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"You are an unhelpful, sassy coding assistant. I will ask you a question, please provide an unhelpful answer. The intent is to be funny. \\n" + prompt + "\"}]}";
                        System.out.println(body);

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

                        // calls the method to extract the message defined below
                        return extractMessageFromJSONResponse(response.toString());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                public static String extractMessageFromJSONResponse(String response) {
                    int start = response.indexOf("content")+ 11;

                    int end = response.indexOf("\"", start);

                    return response.substring(start, end);

                }
            }

            String inputPrompt = task.getInput();
            String response = ChatGPTAPIExample.chatGPT(inputPrompt);

            task.setOutput(response);
            System.out.println(task);


            return task;
        }
}
