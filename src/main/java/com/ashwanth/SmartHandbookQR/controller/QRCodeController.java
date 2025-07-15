package com.ashwanth.SmartHandbookQR.controller;

import com.ashwanth.SmartHandbookQR.model.ContentItem;
import com.ashwanth.SmartHandbookQR.service.FirestoreContentService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
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
    public void generateQRCode(@PathVariable String id,
                               @RequestParam String email,
                               HttpServletResponse response) throws Exception {

        ContentItem item = contentService.getById(id, email);
        String rawToken = item.getId() + "|" + item.getUserEmail();
        String encodedToken = Base64.getEncoder().encodeToString(rawToken.getBytes());

        String qrUrl = "https://yourfrontend.com/view.html?tkn=" + encodedToken;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(qrUrl, BarcodeFormat.QR_CODE, 300, 300);

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        OutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
