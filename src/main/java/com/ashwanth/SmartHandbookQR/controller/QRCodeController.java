package com.ashwanth.SmartHandbookQR.controller;

import com.ashwanth.SmartHandbookQR.model.ContentItem;
import com.ashwanth.SmartHandbookQR.service.FirestoreContentService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.datamatrix.DataMatrixWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QRCodeController {

    private final FirestoreContentService contentService;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public void generateDataMatrix(@PathVariable String id,
                                   @RequestParam String email,
                                   HttpServletResponse response) throws Exception {

        // Fetch content
        ContentItem item = contentService.getById(id, email);
        String rawToken = item.getId() + "|" + item.getUserEmail();
        String encodedToken = Base64.getEncoder().encodeToString(rawToken.getBytes());

        String dataUrl = "https://inklink-720725541229.asia-south1.run.app/view.html?tkn=" + encodedToken;

        // Generate Data Matrix
        DataMatrixWriter dataMatrixWriter = new DataMatrixWriter();
        var bitMatrix = dataMatrixWriter.encode(dataUrl, BarcodeFormat.DATA_MATRIX, 300, 300);

        // Write to response
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        }
    }
}
