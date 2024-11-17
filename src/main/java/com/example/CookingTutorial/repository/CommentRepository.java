package com.example.CookingTutorial.repository;

import com.example.CookingTutorial.entity.CommentPost;
import com.example.CookingTutorial.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentPost, Long> {
    List<CommentPost> findByPost(Post post);
}