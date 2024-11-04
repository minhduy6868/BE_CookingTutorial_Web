package com.example.CookingTutorial.reponsitory;

import com.example.CookingTutorial.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReponsitory extends JpaRepository<Post, String> {
}
