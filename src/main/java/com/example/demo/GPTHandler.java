package com.example.demo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GPTHandler {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    @Value("${openai.api.key}") // OpenAI API 키를 application.properties에서 읽어옵니다.
    private String apiKey;

    private final OkHttpClient httpClient;

    public GPTHandler() {
        this.httpClient = new OkHttpClient.Builder()
                .build();
    }

    public String getResponse(String prompt) throws IOException {
        // Prepare JSON request body
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");

        JsonArray messages = new JsonArray();
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "You are a helpful assistant.");
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);

        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 150);

        // Create HTTP request
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBody.toString(), JSON_MEDIA_TYPE))
                .build();

        // Execute HTTP request
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonObject jsonResponse = com.google.gson.JsonParser.parseString(response.body().string()).getAsJsonObject();
                JsonObject messageObject = jsonResponse
                        .getAsJsonArray("choices")
                        .get(0)
                        .getAsJsonObject()
                        .getAsJsonObject("message");
                return messageObject.get("content").getAsString().trim();
            } else {
                throw new IOException("Error from GPT API: " + response.message());
            }
        }
    }
}
