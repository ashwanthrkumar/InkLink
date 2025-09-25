//package com.ashwanth.SmartHandbookQR;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.io.InputStream;
//
//@Configuration
//public class FirebaseConfig {
//
//    @PostConstruct
//    public void init() {
//        try {
//            InputStream serviceAccount = getClass().getResourceAsStream("/firebase-service-account.json");
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setStorageBucket("smarthandbookqr.firebasestorage.app")
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

package com.ashwanth.SmartHandbookQR;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String PROJECT_ID = "smarthandbookqr"; // 🔹 your GCP project ID
    private static final String STORAGE_BUCKET = "smarthandbookqr.firebasestorage.app"; // 🔹 your Firebase Storage bucket

    @PostConstruct
    public void init() {
        try {
            // 1. Load ADC credentials
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

            // 2. Build FirebaseOptions using ADC credentials and explicit projectId + storage bucket
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId(PROJECT_ID)
                    .setStorageBucket(STORAGE_BUCKET)
                    .build();

            // 3. Initialize Firebase only once
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized using strict ADC. Project: " + PROJECT_ID);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase using ADC", e);
        }
    }
}
