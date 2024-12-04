package com.example.CookingTutorial.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String fullName;
    String avatar;
    String description;
    String phoneNumber;
    String address;
    Set<String> roles;
    List<com.example.CookingTutorial.entity.LikePost> likePosts;
    List<com.example.CookingTutorial.entity.Post> Post;
}
