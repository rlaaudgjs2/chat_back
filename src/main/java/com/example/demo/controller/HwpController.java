package com.example.demo.controller;

import com.example.demo.service.HwpFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/hwp")
public class HwpController {

    @Autowired
    private HwpFileService hwpFileService;

    @PostMapping("/extract")
    public String extractTextFromHwp(@RequestParam("file") MultipartFile file) {
        File tempFile = null;
        try {
            // 업로드된 파일을 임시 파일로 저장
            tempFile = File.createTempFile("upload", ".hwp");
            file.transferTo(tempFile);

            // HWP 파일에서 텍스트 추출
            return hwpFileService.getHwpText(tempFile.getAbsolutePath());
        } catch (Exception e) {
            // 예외 로깅
            System.err.println("HWP 파일 텍스트 추출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "텍스트 추출에 실패했습니다: " + e.getMessage();
        } finally {
            // 임시 파일 삭제
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
