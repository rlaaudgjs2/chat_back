package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {
    private static final String CHAT_GPT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "";

    public String getChatResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Request Body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new Object[]{
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", prompt)
        });

        // HTTP Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // ChatGPT API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity(CHAT_GPT_URL, request, Map.class);
        return (String) ((Map) ((Map) ((List) response.getBody().get("choices")).get(0)).get("message")).get("content");
    }
}
