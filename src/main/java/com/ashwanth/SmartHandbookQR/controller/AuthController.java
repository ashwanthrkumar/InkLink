package com.ashwanth.SmartHandbookQR.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.ashwanth.SmartHandbookQR.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*") // Allow frontend calls (optional - configure properly for prod)
public class AuthController {

    private final GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            GoogleIdToken.Payload payload = googleAuthService.verifyToken(token);

            // Extract user info
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "name", name,
                    "picture", picture,
                    "message", "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}