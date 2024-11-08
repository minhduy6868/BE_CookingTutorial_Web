package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.request.AuthenticationRequest;
import com.example.CookingTutorial.dto.request.IntrospectRequest;
import com.example.CookingTutorial.dto.response.Response;
import com.example.CookingTutorial.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/login") // tạo token khi đăng nhập
    Response<?> authenticate(@RequestBody AuthenticationRequest request){
        var result=authenticationService.authenticate(request);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Login success!")
                .data(result)
                .build();
    }
    @PostMapping("/introspect") // xác thực token còn sống hay k
    Response<?> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result=authenticationService.introspect(request);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("token is still correct")
                .data(result)
                .build();
    }
}
