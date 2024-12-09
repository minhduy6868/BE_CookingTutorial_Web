package com.example.CookingTutorial.dto.response;


import com.example.CookingTutorial.dto.request.PostDTO;
import com.example.CookingTutorial.entity.LikePost;
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
    List<PostDTO> likePosts;
    List<PostDTO> Post;
}
