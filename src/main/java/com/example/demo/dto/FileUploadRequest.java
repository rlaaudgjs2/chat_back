package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
    private String groupName;
    private String folderName;
    private MultipartFile file;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
