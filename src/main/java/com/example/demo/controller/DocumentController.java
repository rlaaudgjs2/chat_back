package com.example.demo.controller;

import com.example.demo.service.ElasticsearchService;
import com.example.demo.service.HwpFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private HwpFileService hwpFileService;

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                 @RequestParam("indexName") String indexName,
                                 @RequestParam("title") String title) {
        try {
            File tempFile = File.createTempFile("upload", ".hwp");
            file.transferTo(tempFile);

            String content = hwpFileService.getHwpText(tempFile.getAbsolutePath());
            elasticsearchService.saveDocument(indexName, title, content);

            tempFile.delete();
            return "파일이 성공적으로 업로드되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "업로드 중 오류 발생: " + e.getMessage();
        }
    }

    @GetMapping("/search")
    public String searchDocument(@RequestParam("indexName") String indexName,
                                 @RequestParam("query") String query) {
        try {
            return elasticsearchService.searchDocuments(indexName, query);
        } catch (Exception e) {
            e.printStackTrace();
            return "검색 중 오류 발생: " + e.getMessage();
        }
    }
}
