package com.example.demo;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.HWPCharNormal;
import kr.dogfoot.hwplib.reader.HWPReader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class HwpFileHandler {
    public static String extractText(File hwpFile) throws IOException {
        try {
            // HWP 파일 읽기
            HWPFile hwp = Optional.ofNullable(HWPReader.fromFile(hwpFile.getAbsolutePath()))
                    .orElseThrow(() -> new IllegalArgumentException("HWP 파일을 읽을 수 없습니다."));

            // 섹션별 텍스트 추출 및 병합
            return hwp.getBodyText().getSectionList().stream()
                    .flatMap(section -> section.getParagraphCount() > 0
                            ? java.util.stream.IntStream.range(0, section.getParagraphCount())
                            .mapToObj(section::getParagraph)
                            : java.util.stream.Stream.empty())
                    .map(HwpFileHandler::extractParagraphText)
                    .filter(text -> text != null && !text.isEmpty())
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new IOException("HWP 파일 텍스트 추출 중 오류 발생", e);
        }
    }

    private static String extractParagraphText(Paragraph paragraph) {
        return Optional.ofNullable(paragraph.getText())
                .map(text -> text.getCharList().stream()
                        .filter(ch -> ch instanceof HWPCharNormal)
                        .map(ch -> String.valueOf(((HWPCharNormal) ch).getCode()))
                        .collect(Collectors.joining()))
                .orElse("");
    }
}