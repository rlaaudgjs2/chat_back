package com.example.demo.service;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class HwpFileService {

    public String getHwpText(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || !filePath.endsWith(".hwp")) {
            throw new IllegalArgumentException("유효하지 않은 HWP 파일 경로");
        }

        HWPFile hwpFile = HWPReader.fromFile(file.getAbsolutePath());
        if (hwpFile == null) {
            throw new IllegalArgumentException("HWP 파일 읽기에 실패");
        }

        TextExtractMethod method = TextExtractMethod.InsertControlTextBetweenParagraphText;
        return TextExtractor.extract(hwpFile, method);
    }
}
