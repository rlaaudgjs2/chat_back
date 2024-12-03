package com.example.demo.dto;

public class ChatResponse {
    private String result; // ChatGPT의 응답 텍스트

    public ChatResponse() {
    }

    public ChatResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
