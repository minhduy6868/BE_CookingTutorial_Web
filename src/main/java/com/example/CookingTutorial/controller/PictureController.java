package com.example.CookingTutorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import com.example.CookingTutorial.service.CloudinaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cloudinary")
@RequiredArgsConstructor
public class PictureController {

    private final CloudinaryService cloudinaryService;

    // API tải ảnh lên và trả về URL
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        // Lấy URL của ảnh từ CloudinaryService
        String imageUrl = cloudinaryService.uploadImage(file);
        
        // Trả về URL của ảnh
        return new ResponseEntity<>(imageUrl, HttpStatus.OK);
    }
}
