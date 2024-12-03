package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final FirebaseService firebaseService;
    private final ElasticsearchService elasticsearchService;
    private final ChatGPTService chatGPTService;

    public ChatService(FirebaseService firebaseService, ElasticsearchService elasticsearchService, ChatGPTService chatGPTService) {
        this.firebaseService = firebaseService;
        this.elasticsearchService = elasticsearchService;
        this.chatGPTService = chatGPTService;
    }

    public String processChat(String folderName, String groupName, String query) throws Exception {
        // 1. Firestore에서 파일 목록 가져오기
        String indexName = folderName + "_" + groupName; // Elasticsearch 색인 이름 구성

        // 2. Elasticsearch를 통해 문서 검색
        String searchResult = elasticsearchService.searchDocuments(indexName, query);

        // 3. ChatGPT API를 통해 응답 생성
        return chatGPTService.getChatResponse(searchResult);
    }
}
