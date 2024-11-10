package com.example.tutorial.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tutorial.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    
}
