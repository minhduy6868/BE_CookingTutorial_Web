package com.example.CookingTutorial.repository;

import com.example.CookingTutorial.entity.LikePost;
import com.example.CookingTutorial.entity.Post;
import com.example.CookingTutorial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByUserAndPost(User user, Post post);
}
