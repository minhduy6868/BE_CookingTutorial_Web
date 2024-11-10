package com.example.CookingTutorial.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;
    String linkVideo;
    String title;
    String description;
    String tutorial;

    @OneToMany(mappedBy = "post")
    List<Ingredient> ingredients;

    @OneToMany(mappedBy = "post")
    List<Picture> pictures;


    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
