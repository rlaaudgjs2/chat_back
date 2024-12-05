package com.example.demo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ElasticsearchClient elasticsearchClient;
    private final GPTHandler gptHandler;

    @Autowired
    public ApiController(ElasticsearchClient elasticsearchClient, GPTHandler gptHandler) {
        this.elasticsearchClient = elasticsearchClient;
        this.gptHandler = gptHandler;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("indexName") String indexName) throws IOException {
        // Save file temporarily in a proper directory
        File tempFile = Files.createTempFile("upload_", ".hwp").toFile();

        try {
            file.transferTo(tempFile);

            // Extract text
            String content = HwpFileHandler.extractText(tempFile);

            // Prepare index document
            Map<String, Object> document = new HashMap<>();
            document.put("content", content);
            document.put("title", file.getOriginalFilename());

            // Upload to Elasticsearch
            IndexResponse response = elasticsearchClient.index(i -> i
                    .index(indexName)
                    .document(document)
            );

            return "File uploaded successfully! Document ID: " + response.id();
        } catch (Exception e) {
            return "Upload failed: " + e.getMessage();
        } finally {
            // Ensure temp file is deleted
            if (tempFile.exists() && !tempFile.delete()) {
                System.err.println("Failed to delete temp file: " + tempFile.getAbsolutePath());
            }
        }
    }

    @PostMapping("/chat")
    public String chat(@RequestBody JsonObject requestBody) {
        try {
            String text = requestBody.get("text").getAsString();
            String indexName = requestBody.get("index_name").getAsString();

            // Step 1: Search Elasticsearch
            SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s
                    .index(indexName)
                    .query(q -> q
                            .match(m -> m
                                    .field("content")
                                    .query(text)
                            )
                    ), Map.class
            );

            // Step 2: Extract result or default response
            String documentContent = searchResponse.hits().hits().isEmpty()
                    ? "No relevant documents found in Elasticsearch."
                    : searchResponse.hits().hits().get(0).source().get("content").toString();

            // Step 3: Generate GPT response
            String gptPrompt = String.format(
                    "Based on the following document content, answer the query:\n" +
                            "Document: %s\n" +
                            "Query: %s", documentContent, text
            );

            String gptResponse = gptHandler.getResponse(gptPrompt);

            // Return GPT response
            return gptResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while processing the request: " + e.getMessage();
        }
    }
}