package com.example.demo.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FirebaseService {

    private static final String COLLECTION_NAME = "documents";

    public List<String> getFiles(String folderName, String groupName) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        String documentId = folderName + "_" + groupName;

        DocumentReference docRef = db.collection(COLLECTION_NAME).document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> data = document.getData();
            if (data != null && data.containsKey("file")) {
                return (List<String>) data.get("file");
            }
        }
        return new ArrayList<>(); // 파일이 없으면 빈 리스트 반환
    }
}
