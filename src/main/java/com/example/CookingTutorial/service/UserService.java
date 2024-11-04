package com.example.CookingTutorial.service;

import com.example.CookingTutorial.dto.reponse.UserReponse;
import com.example.CookingTutorial.dto.request.UserCreateRequest;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.reponsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User createUser(UserCreateRequest request){
        User user = new User();

        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        user.setDescription(request.getDescription());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
