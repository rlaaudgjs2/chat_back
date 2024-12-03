package com.example.demo.dto;

public class ChatRequest {
    private String indexName; // Elasticsearch 인덱스 이름
    private String text;      // 사용자가 입력한 텍스트

    public ChatRequest() {
    }

    public ChatRequest(String indexName, String text) {
        this.indexName = indexName;
        this.text = text;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
