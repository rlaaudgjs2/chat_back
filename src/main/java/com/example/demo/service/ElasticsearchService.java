package com.example.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ElasticsearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    public void saveDocument(String indexName, String title, String content) throws IOException {
        elasticsearchClient.index(IndexRequest.of(i -> i
                .index(indexName)
                .document(new Document(title, content))
        ));
    }

    public String searchDocuments(String indexName, String queryText) throws IOException {
        SearchRequest request = SearchRequest.of(s -> s
                .index(indexName)
                .query(Query.of(q -> q
                        .match(m -> m
                                .field("content")
                                .query(queryText)
                        )
                ))
        );

        SearchResponse<Document> response = elasticsearchClient.search(request, Document.class);
        if (!response.hits().hits().isEmpty()) {
            return response.hits().hits().get(0).source().getContent();
        }
        return "관련 문서를 찾을 수 없습니다.";
    }

    static class Document {
        private String title;
        private String content;

        public Document(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }
}

