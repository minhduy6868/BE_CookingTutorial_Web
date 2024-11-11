package com.example.CookingTutorial.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // Phương thức tải ảnh lên và trả về URL
    public String uploadImage(MultipartFile file) {
        try {
            // Tải ảnh lên Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            // Trả về URL của ảnh
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
}