package com.example.CookingTutorial.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreateRequest {
//    String linkVideo;
    String title;
    String description;
    String tutorial;
    String typePost;
}
