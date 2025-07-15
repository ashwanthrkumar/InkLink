package com.ashwanth.SmartHandbookQR.controller;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class UploadController {

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("email") String email) throws IOException {

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String path = String.format("content/%s/images/%s", email, filename);

        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(path, file.getBytes(), file.getContentType());

        // Make it public
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        // Return public URL
        String url = "https://storage.googleapis.com/" + bucket.getName() + "/" + blob.getName();
        return ResponseEntity.ok(Map.of("url", url));
    }
}

