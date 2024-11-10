package com.example.tutorial.Service.imp;

import java.util.List;

import com.example.tutorial.DTO.UserDTO;
import com.example.tutorial.payload.reqquest.SignUpRequest;

public interface LoginServiceImp {
    List<UserDTO> getAllUser();

    boolean checkLogin(String username, String password);
    boolean addUser(SignUpRequest signUpRequest);
}
