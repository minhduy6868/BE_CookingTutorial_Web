package com.example.CookingTutorial.repository;

import com.example.CookingTutorial.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByTitleContainingIgnoreCase(String title); // tìm kiếm k phân biệt chữ hoa chữ thường

    @Query(value = "SELECT * FROM post ORDER BY dislike_count DESC LIMIT :limit", nativeQuery = true)
    List<Post> findTopPostsByDislikeCount(int limit);
}
