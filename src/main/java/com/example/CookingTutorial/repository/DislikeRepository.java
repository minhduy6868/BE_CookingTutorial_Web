package com.example.CookingTutorial.repository;

import com.example.CookingTutorial.entity.DislikePost;
import com.example.CookingTutorial.entity.Post;
import com.example.CookingTutorial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DislikeRepository extends JpaRepository<DislikePost, Long> {
    Optional<DislikePost> findByUserAndPost(User user, Post post);
}
