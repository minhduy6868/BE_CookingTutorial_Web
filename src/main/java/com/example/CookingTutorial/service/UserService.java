package com.example.CookingTutorial.service;

import com.example.CookingTutorial.dto.request.*;
import com.example.CookingTutorial.dto.response.UserResponse;
import com.example.CookingTutorial.entity.Post;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.enums.Role;
import com.example.CookingTutorial.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Service
@Slf4j

public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CloudinaryService cloudinaryService;

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

    public boolean checkEmail(String email){
        return userRepository.findByEmail(email).isEmpty();
    }

    // send otp to email
    private String sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        return message.getText();
    }

    private Map<String, String> otpStorage = new HashMap<>();
    public int codeEmail(UserForgotPassRequest request){

        if(checkEmail(request.getEmail())){
            return 0;
        }

        Random r = new Random();
        int randomNumber = r.nextInt(100000, 999999+1);

        otpStorage.put(request.getEmail(), String.valueOf(randomNumber)); // lưu otp và gmail lại với nhau

        return Integer.parseInt(sendEmail(request.getEmail(),"Mã kích hoạt", String.valueOf(randomNumber)));
    }

    // change password
    public int changePass(ChangePasswordRequest request){

        if(userRepository.findByEmail(request.getEmail()).isEmpty()){
            return 0;
        }

        // Kiểm tra email và OTP có khớp không
        if (!otpStorage.containsKey(request.getEmail()) || !Objects.equals(otpStorage.get(request.getEmail()), request.getOtp())) {
            return 0; // Trả về lỗi nếu OTP không hợp lệ
        }

        User user = userRepository.findByEmail(request.getEmail()).get();
        user.setPassword(new BCryptPasswordEncoder(10).encode(request.getNewPassword()));
        userRepository.save(user);

        otpStorage.remove(request.getEmail()); // xóa cái otp

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
    public UserResponse getInfo(String userId){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Not find user!"));
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

    public boolean updateAvatar(String email, MultipartFile image){
        Optional<User> userOpt = userRepository.findByEmail(email);

        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setAvatar(cloudinaryService.uploadImage(image));
            userRepository.save(user);
            log.info("User with email {} deleted successfully.", email);
            return true;
        }
        log.warn("User with email {} not found for deletion.", email);
        return false;
    }

    public User updateUser(String email, UserUpdateRequest request){
        // Lấy user từ email
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Chỉ cập nhật trường nếu giá trị không phải là null
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getDescription() != null) {
            user.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        // Lưu lại đối tượng user sau khi cập nhật
        userRepository.save(user);
        return user;
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUser(){
        log.info("In method getUsers!");
        return userRepository.findAll();
    }

    public int numberOfUser(){
        List<User> list=userRepository.findAll();
        return list.size();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteUser(String userId){
        if(userRepository.findById(userId).isEmpty()){
            return false;
        }
        userRepository.deleteById(userId);
        return true;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User updateUserByAdmin(String userId, AdminUpdateUserRequest request){

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));;

        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setAvatar(request.getAvatar());
        user.setDescription(request.getDescription());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder(10).encode(request.getPassword())); // Chỉ mã hóa và cập nhật nếu có mật khẩu mới
        }
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            HashSet<String> roles = new HashSet<>();
            roles.add(request.getRoles().toUpperCase());
            user.setRoles(roles);
        }
        userRepository.save(user);

        return user;
    }





    // method other
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


}
