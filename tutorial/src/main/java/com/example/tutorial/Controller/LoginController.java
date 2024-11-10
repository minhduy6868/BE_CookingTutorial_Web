package com.example.tutorial.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.tutorial.Service.imp.LoginServiceImp;
import com.example.tutorial.Utils.JwtUtilsHelper;
import com.example.tutorial.payload.ResponseData;
import com.example.tutorial.payload.reqquest.SignUpRequest;



@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginServiceImp  loginServiceImp;

    @Autowired
    JwtUtilsHelper jwtUtilsHelper;

    @GetMapping("/users")
    public ResponseEntity<?> signin(){
        ResponseData responseData = new ResponseData();

        responseData.setData(loginServiceImp.getAllUser());

       
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestParam String email, @RequestParam String password){
        ResponseData resPonseData = new ResponseData();

        if(loginServiceImp.checkLogin(email, password)){
            String token = jwtUtilsHelper.generrateToken(email);
            resPonseData.setData(token);
            
        }else{
            resPonseData.setData("");
            resPonseData.setSuccess(false);
        }


        return new ResponseEntity<>(resPonseData, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest){
        ResponseData resPonseData = new ResponseData();
        
        resPonseData.setData(loginServiceImp.addUser(signUpRequest));

        return new ResponseEntity<>(resPonseData, HttpStatus.OK);
    }
    
}
