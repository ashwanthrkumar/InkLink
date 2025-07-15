package com.ashwanth.SmartHandbookQR.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.ashwanth.SmartHandbookQR.model.ContentItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.cloud.Timestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirestoreContentService {

   // private final String COLLECTION_NAME = "content";
    public List<ContentItem> getContentForUser(String email) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference entries = firestore
                .collection("content")
                .document(email)
                .collection("entries");

        ApiFuture<QuerySnapshot> future = entries.get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        List<ContentItem> items = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            ContentItem item = doc.toObject(ContentItem.class);
            item.setId(doc.getId());
            item.setUserEmail(email);
            items.add(item);
           // System.out.print(item);
        }
        return items;
    }

//    public List<ContentItem> getAllContent() throws Exception {
//        Firestore firestore = FirestoreClient.getFirestore();
//        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
//        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//
//        List<ContentItem> items = new ArrayList<>();
//        for (QueryDocumentSnapshot doc : documents) {
//            ContentItem item = new ContentItem();
//            item.setId(doc.getId());
//            item.setTitle(doc.getString("title"));
//            item.setType(doc.getString("type"));
//            item.setContent(doc.getString("content"));
//
//            item.setCreatedAt(doc.getTimestamp("createdAt"));
//
//
//            items.add(item);
//        }
//        return items;
//    }

    public ContentItem getById(String id, String email) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();

        // Correct path: content/{email}/entries/{id}
        DocumentReference docRef = firestore
                .collection("content")
                .document(email)
                .collection("entries")
                .document(id);

        DocumentSnapshot doc = docRef.get().get();

        if (!doc.exists()) throw new Exception("Not found");

        ContentItem item = new ContentItem();
        item.setId(doc.getId());
        item.setTitle(doc.getString("title"));
        item.setType(doc.getString("type"));
        item.setContent(doc.getString("content"));
        item.setUserEmail(doc.getString("userEmail"));
        // Convert Firestore Timestamp to LocalDateTime
        Timestamp ts = doc.getTimestamp("createdAt");
        if (ts != null) {
            item.setCreatedAt(item.getCreatedAt());
        }

        return item;
    }


    public ContentItem saveContent(ContentItem item) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();

        item.setCreatedAt(Timestamp.now());

        Map<String, Object> data = new HashMap<>();
        data.put("title", item.getTitle());
        data.put("type", item.getType());
        data.put("content", item.getContent());
        data.put("createdAt", item.getCreatedAt()); // ✅ uses Timestamp
        data.put("userEmail",item.getUserEmail());

        // Save under: content/{email}/entries/{auto-id}
        DocumentReference ref = firestore
                .collection("content")
                .document(item.getUserEmail())
                .collection("entries")
                .document();

        ref.set(data);

        item.setId(ref.getId());
        return item;
    }


    public void deleteContent(String email, String id) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference docRef = firestore
                .collection("content")
                .document(email)
                .collection("entries")
                .document(id);

        DocumentSnapshot snapshot = docRef.get().get();
        if (!snapshot.exists()) {
            throw new Exception("Document not found for deletion");
        }

        docRef.delete().get(); // Wait for deletion to complete
        System.out.println("Deleted: content/" + email + "/entries/" + id);
    }

    public void updateContent(String id, String email, Map<String, Object> updates) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference docRef = firestore
                .collection("content")
                .document(email)
                .collection("entries")
                .document(id);
        docRef.update(updates);
    }


//public ResponseEntity<ContentItem> update(ContentItem item){
//    Firestore firestore = FirestoreClient.getFirestore();
//    DocumentReference docRef = firestore
//            .collection("content")
//            .document(item.getUserEmail())
//            .collection("entries")
//            .document(item.getId());
//
//    Map<String, Object> data = new HashMap<>();
//    data.put("title", item.getTitle());
//    data.put("type", item.getType());
//    data.put("content", item.getContent());
//    data.put("createdAt", Timestamp.now());
//
//    docRef.set(data).get();
//
//    item.setCreatedAt(Timestamp.now()); // set recent date for frontend
//    return ResponseEntity.ok(item);
//
//    //return ResponseEntity.ok("Updated");
//}

}
