package com.example.CookingTutorial.dto.request;

import com.example.CookingTutorial.entity.Post;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUpdateUserRequest {
    String email;
    String password;
    String fullName;
    String avatar;
    String description;
    String phoneNumber;
    String address;
    String roles;
//    List<Post> Post;
}
