package com.example.CookingTutorial.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String email;
    String password;
    String fullName;
    String avatar;
    String description;
    String phoneNumber;
    String address;
    Set<String> roles;

    @OneToMany(mappedBy = "user")
    List<Post> Post;
}
