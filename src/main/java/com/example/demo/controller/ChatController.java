package com.example.demo.controller;

import com.example.demo.service.FirebaseService;
import com.example.demo.service.ElasticsearchService;
import com.example.demo.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping
    public String chat(@RequestParam String folderName, @RequestParam String groupName, @RequestParam String query) {
        try {
            // Firebase에서 문서 리스트 가져오기
            List<String> files = firebaseService.getFiles(folderName, groupName);

            // Elasticsearch에서 각 파일에 대해 검색
            for (String indexName : files) {
                String searchResult = elasticsearchService.searchDocuments(indexName, query);
                if (!"관련 문서를 찾을 수 없습니다.".equals(searchResult)) {
                    // 관련 문서를 찾으면 ChatGPT API 호출
                    return chatGPTService.getChatResponse(searchResult);
                }
            }

            // 관련 문서를 찾지 못한 경우
            return "죄송합니다. 그 내용은 문서에 있지 않습니다.";
        } catch (Exception e) {
            return "처리 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}

