package com.example.CookingTutorial.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;
    String ingredientName;
    String quantity;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

}
