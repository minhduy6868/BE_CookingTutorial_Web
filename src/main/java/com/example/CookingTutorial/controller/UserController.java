package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.request.DKRequest;
import com.example.CookingTutorial.dto.response.Response;
import com.example.CookingTutorial.dto.request.UserCreateRequest;
import com.example.CookingTutorial.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping // tạo mới 1 user
    Response<?> createUser(@RequestBody UserCreateRequest request){
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Create user successfull")
                .data(userService.createUser(request))
                .build();
    }

    @GetMapping // show ra hết tất cả các user
    Response<?> getUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Email: " + authentication.getName());

        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));


        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get users successfull")
                .data(userService.getUsers())
                .build();
    }
    @GetMapping("/{userId}") // show ra user
    Response<?> getUser(@PathVariable("userId") String userId){
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get user by id successfull")
                .data(userService.getUser(userId))
                .build();
    }


    @GetMapping("/myInfo") // show ra info chính mình
    Response<?> getMyInfo(){
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get my info successfull!")
                .data(userService.getMyInfo())
                .build();
    }

    @GetMapping("/getAllUser")
    Response<?> getAllUser(){
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get users successfull")
                .data(userService.getAllUser())
                .build();
    }

    @PostMapping("/DKUser")
    Response<?> DKUser(@RequestBody DKRequest request){
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Register successfull!")
                .data(userService.DKUser(request))
                .build();
    }

//    @PostMapping
//    Response<?> forgotPass(){
//        return Response.builder().build();
//    }
}
