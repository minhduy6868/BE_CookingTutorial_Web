package com.example.CookingTutorial.repository;

import com.example.CookingTutorial.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureReponsitory extends JpaRepository<Picture, String> {
}
