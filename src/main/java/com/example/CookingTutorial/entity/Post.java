package com.example.CookingTutorial.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String linkVideo;
    String title;
    String description;
    String tutorial;
    String typePost;
    int likeCount=0;
    int dislikeCount=0;


    @ElementCollection
    List<User> listUserLike;

    @ElementCollection
    List<User> listUserDislike;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Ingredient> ingredients;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("post")
    List<CommentPost> commentPosts;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("post")
    List<Picture> pictures;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("post") // bỏ qua thuộc tính post có trong user
    User user;

}
