package com.example.tutorial.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tutorial.Service.LoginService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    LoginService userServiceImp;

    @GetMapping("")
    public ResponseEntity<?> getAllUSer(){

        return new ResponseEntity<>(userServiceImp.getAllUser(), HttpStatus.OK);
    }

    @Autowired
    private LoginService loginService;

    // API lấy số lượng người dùng và số bài đăng
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Long>> getCounts() {
        Map<String, Long> counts = loginService.getCount();
        return ResponseEntity.ok(counts);
    }
}
