package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.reponse.UserReponse;
import com.example.CookingTutorial.dto.request.UserCreateRequest;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    UserReponse createUser(@RequestBody UserCreateRequest request){

        return new UserReponse<>(HttpStatus.OK.value(),"successfull!",userService.createUser(request));
    }

    @GetMapping
    UserReponse getUsers(){
        return new UserReponse<>(HttpStatus.OK.value(),"successfull!", userService.getUsers());
    }

}
