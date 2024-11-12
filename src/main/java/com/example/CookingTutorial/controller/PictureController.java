package com.example.CookingTutorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.example.CookingTutorial.service.CloudinaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cloudinary")
@RequiredArgsConstructor
public class PictureController {

    private final CloudinaryService cloudinaryService;

    // API tải ảnh lên và trả về URL dưới dạng JSON
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
        // Lấy URL của ảnh từ CloudinaryService
        String imageUrl = cloudinaryService.uploadImage(file);
        
        // Tạo phản hồi JSON với URL của ảnh
        Map<String, String> response = new HashMap<>();
        response.put("url", imageUrl);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
