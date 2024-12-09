package com.example.CookingTutorial.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CommentPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String content;

    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"user", "commentPosts"})
    Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"post", "likePost"})
    User user;
}
