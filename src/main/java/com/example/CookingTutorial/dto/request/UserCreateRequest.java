package com.example.CookingTutorial.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    String email;
    String password;
    String fullName;
    String avatar;
    String description;
    String phoneNumber;
    String address;
}
