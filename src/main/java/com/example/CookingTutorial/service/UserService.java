package com.example.CookingTutorial.service;

import com.example.CookingTutorial.dto.request.UserCreateRequest;
import com.example.CookingTutorial.dto.response.UserResponse;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.enums.Role;
import com.example.CookingTutorial.reponsitory.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
    @PreAuthorize("hasRole('ADMIN')") // phải cso quyền mới được vào
    public List<User> getUsers(){
        log.info("In method getUsers!");
        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.email == authentication.name") // kiểm tra đúng email kia mới cho kiểm tra
    public User getUser(String userId){
        log.info("In method get user by id!");
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found!"));
    }
}
