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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            // 1. Get the base64 encoded env variable
            String firebaseJson = System.getenv("FIREBASE_SERVICE_ACCOUNT");

//            if (firebaseJsonBase64 == null || firebaseJsonBase64.isEmpty()) {
//                throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT env variable not set or empty");
//            }

            // 2. Decode the Base64 string to get JSON content
//            byte[] decodedBytes = Base64.getDecoder().decode(firebaseJsonBase64);
//            InputStream serviceAccount = new ByteArrayInputStream(decodedBytes);

            // 2. Convert it to InputStream
           InputStream serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));

            // 3. Use it for Firebase initialization
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("smarthandbookqr.firebasestorage.app")
                    .build();

            // 4. Initialize Firebase only once
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized from ENV variable.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
