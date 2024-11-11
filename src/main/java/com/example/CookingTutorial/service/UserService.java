package com.example.CookingTutorial.service;

import com.example.CookingTutorial.dto.request.ChangePasswordRequest;
import com.example.CookingTutorial.dto.request.DKRequest;
import com.example.CookingTutorial.dto.request.UserCreateRequest;
import com.example.CookingTutorial.dto.request.UserForgotPassRequest;
import com.example.CookingTutorial.dto.response.UserResponse;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.enums.Role;
import com.example.CookingTutorial.reponsitory.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@Slf4j

public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;


    //For User

    public String DKUser(DKRequest request){
        User user =new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(new BCryptPasswordEncoder(10).encode(request.getPassword()));

        userRepository.save(user);
        return "Register successfull!";
    }
    public UserResponse createUser(UserCreateRequest request){
        User user = new User();

        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        user.setDescription(request.getDescription());
        user.setPassword(new BCryptPasswordEncoder(10).encode(request.getPassword())); // mã hóa mật khẩu
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        userRepository.save(user);
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .description(user.getDescription())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .roles(user.getRoles())
                .Post(user.getPost())
                .build();
    }

    // send otp to email
    String sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        return message.getText();
    }
    public int codeEmail(UserForgotPassRequest request){
        Random r = new Random();
        int randomNumber = r.nextInt(100000, 999999+1);
        if(userRepository.findByEmail(request.getEmail()).isEmpty()){
            return 0;
        }
        return Integer.parseInt(sendEmail(request.getEmail(),"Mã kích hoạt", String.valueOf(randomNumber)));
    }

    // change password
    public int changePass(ChangePasswordRequest request){
        if(userRepository.findByEmail(request.getEmail()).isEmpty()){
            return 0;
        }

        return 1;
    }
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name=context.getAuthentication().getName();

        User user = userRepository.findByEmail(name).orElseThrow(()->new RuntimeException("Not find email!"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .description(user.getDescription())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .roles(user.getRoles())
                .Post(user.getPost())
                .build();
    }

    @PostAuthorize("returnObject.email == authentication.name") // kiểm tra đúng email kia mới cho kiểm tra
    public User getUser(String userId){
        log.info("In method get user by id!");
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found!"));
    }

    // Xóa tài khoản của người dùng theo email
    public boolean deleteAccountByEmail(String email) {
        // Tìm người dùng theo email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            // Xóa người dùng
            User user = userOpt.get();
            userRepository.deleteById(user.getId());
            log.info("User with email {} deleted successfully.", email);
            return true;  // Thành công
        }
        log.warn("User with email {} not found for deletion.", email);
        return false;  // Không tìm thấy người dùng
    }



    //For Admin
    @PreAuthorize("hasRole('ADMIN')") // phải cso quyền mới được vào
    public List<User> getUsers(){
        log.info("In method getUsers!");
        return userRepository.findAll();
    }
    public List<User> getAllUser(){
        log.info("In method getUsers!");
        return userRepository.findAll();
    }

    public int numberOfUser(){
        List<User> list=userRepository.findAll();
        return list.size();
    }


}
