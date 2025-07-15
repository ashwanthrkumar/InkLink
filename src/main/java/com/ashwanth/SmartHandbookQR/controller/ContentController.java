package com.ashwanth.SmartHandbookQR.controller;

import com.ashwanth.SmartHandbookQR.model.ContentItem;
import com.ashwanth.SmartHandbookQR.service.FirestoreContentService;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContentController {

    private final FirestoreContentService contentService;

    @GetMapping
    public List<ContentItem> getContent(@RequestParam String email) throws Exception {
        return contentService.getContentForUser(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentItem> getById(@PathVariable String id, @RequestParam String email) throws Exception {
        ContentItem item = contentService.getById(id, email);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<ContentItem> getById1(@PathVariable String id, @RequestParam String email) throws Exception {
        ContentItem item = contentService.getById(id, email);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ContentItem saveContent(@RequestBody ContentItem item) throws Exception {
        return contentService.saveContent(item);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteContent(@PathVariable String id, @RequestParam String email) {
        try {
            contentService.deleteContent(email, id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting content: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateContent(@PathVariable String id,
                                           @RequestParam String email,
                                           @RequestBody Map<String, Object> updates) {
        contentService.updateContent(id, email, updates);
        return ResponseEntity.ok().build();
    }


//    @PutMapping("/update")
//    public ResponseEntity<?> updateContent(@RequestBody ContentItem item) throws Exception {
//        // Simply replace the existing doc
//
//        Firestore firestore = FirestoreClient.getFirestore();
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("title", item.getTitle());
//        data.put("type", item.getType());
//        data.put("content", item.getContent());
//        data.put("createdAt", Timestamp.now());
//
//        DocumentReference ref = firestore
//                .collection("content")
//                .document(item.getUserEmail())
//                .collection("entries")
//                .document(item.getId());
//
//        ref.set(data).get();
//
//        item.setCreatedAt(Timestamp.now()); // set recent date for frontend
//        return ResponseEntity.ok(item);
////        Firestore firestore = FirestoreClient.getFirestore();
////        DocumentReference docRef = firestore
////                .collection("content")
////                .document(item.getUserEmail())
////                .collection("entries")
////                .document(item.getId());
////
////        Map<String, Object> data = new HashMap<>();
////        data.put("title", item.getTitle());
////        data.put("type", item.getType());
////        data.put("content", item.getContent());
////        data.put("createdAt", Timestamp.now());
////
////        docRef.set(data);
////
////        return ResponseEntity.ok("Updated");
//    }
//



}
