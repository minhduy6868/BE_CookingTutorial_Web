package com.example.CookingTutorial.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String alt;
    String link;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties("pictures")
    Post post;

}
