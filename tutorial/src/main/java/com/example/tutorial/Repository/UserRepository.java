package com.example.tutorial.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tutorial.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByEmail(String email);
}
