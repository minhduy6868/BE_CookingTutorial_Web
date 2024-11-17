package com.example.CookingTutorial.repository;

import com.example.CookingTutorial.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientReponsitory extends JpaRepository<Ingredient, String> {
}
