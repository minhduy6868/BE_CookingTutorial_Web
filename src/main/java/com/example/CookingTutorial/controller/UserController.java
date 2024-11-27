package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.request.*;
import com.example.CookingTutorial.dto.response.Response;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.service.PostService;
import com.example.CookingTutorial.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    /// For User

    @PostMapping // Tạo mới 1 user
    public Response<?> createUser(@RequestBody UserCreateRequest request) {
        if (userService.checkEmail(request.getEmail())){
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Create user successfully!")
                    .data(userService.createUser(request))
                    .build();
        }
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Create user fail! Email already exists.")
                .build();

    }

    @GetMapping // Lấy tất cả người dùng
    public Response<?> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Email: " + authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get users successfully!")
                .data(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}") // Lấy thông tin người dùng theo ID
    public Response<?> getUser(@PathVariable("userId") String userId) {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get user by id successfully!")
                .data(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo") // Lấy thông tin của người dùng hiện tại
    public Response<?> getMyInfo() {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get my info successfully!")
                .data(userService.getMyInfo())
                .build();
    }
    @GetMapping("/info/{userId}") // Lấy thông tin của người khác dùng khi click vào
    public Response<?> getInfo(@PathVariable("userId") String userId) {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get info successfully!")
                .data(userService.getInfo(userId))
                .build();
    }

    // Đăng ký tài khoản
    @PostMapping("/DKUser")
    public Response<?> DKUser(@RequestBody DKRequest request) {
        if(userService.checkEmail(request.getEmail())){
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Register successfully!")
                    .data(userService.DKUser(request))
                    .build();
        }
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Register fail! Email already exists.")
                .build();
    }



    // Quên mật khẩu
    @PostMapping("/forgotPassword")
    public Response<?> forgotPassword(@RequestBody UserForgotPassRequest request) {
        int result = userService.codeEmail(request);
        Map<String, Object> data=new HashMap<>();
        data.put("OTP: ", result);
        if (result != 0) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Password reset email sent successfully!")
                    .data(data)
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Error sending password reset email. Please try again.")
                    .build();
        }
    }


    @PutMapping("/updatePass")
    public Response<?> updateNewPass(@RequestBody ChangePasswordRequest request){

        if (userService.changePass(request)==1 ) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Change password of email " + request.getEmail()+ " is successfully!")
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Error not find email. Please try again.")
                    .build();
        }

    }


    // Cập nhật thông tin tài khoản
    @PutMapping("/updateUser")
    public Response<?> updateUser(@RequestBody UserUpdateRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("User updated successfully!")
                .data(userService.updateUser(email, request))
                .build();
    }


    // Cập nhật avatar
    @PutMapping("/updateAvatar")
    public Response<?> updateAvatar(@RequestPart("image") MultipartFile avatarUrl) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        boolean isUpdateAvatar = userService.updateAvatar(email, avatarUrl);

        if (isUpdateAvatar) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Update avatar successfully!")
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Update avatar fail!")
                    .build();
        }

    }



    // Xóa tài khoản của chính mình
    @DeleteMapping("/deleteAccount")
    public Response<?> deleteAccount() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        boolean isDeleted = userService.deleteAccountByEmail(email);
        if (isDeleted) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Your account has been deleted successfully.")
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to delete your account. Please try again.")
                    .build();
        }
    }



    /// For Admin

    // Admin có thể xóa 1 hoặc nhiều user, chỉ cần truyền ID vào
   @DeleteMapping("/admin/delete/{userId}")
    public Response<?> deleteUser(@PathVariable("userId") String userId) {
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("User deleted successfully!")
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to delete user!")
                    .build();
        }
    }

    // Admin có thể chỉnh sửa thông tin của 1 user
    @PutMapping("/admin/edit/{userId}")
    public Response<?> updateUserByAdmin(@PathVariable("userId") String userId, @RequestBody AdminUpdateUserRequest request) {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("User information updated successfully!")
                .data(userService.updateUserByAdmin(userId, request))
                .build();
    }



    // Admin lấy số lượng User và bài viết
    @GetMapping("/admin/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> numberOfUser(){
        Map<String, Object> data = new HashMap<>();

        data.put("user", userService.numberOfUser());
        data.put("post", postService.numberOfPost());


        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Admin statistics retrieved successfully.!")
                .data(data)
                .build();
    }


    // Lấy tất cả người dùng (cho admin)
    @GetMapping("/getAllUser")
    public Response<?> getAllUser() {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get all users successfully.")
                .data(userService.getAllUser())
                .build();
    }
}
