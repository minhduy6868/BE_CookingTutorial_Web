package com.example.tutorial.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tutorial.DTO.UserDTO;
import com.example.tutorial.Repository.PostRepository;
import com.example.tutorial.Repository.UserRepository;
import com.example.tutorial.Service.imp.LoginServiceImp;
import com.example.tutorial.entity.Users;
import com.example.tutorial.payload.reqquest.SignUpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService implements LoginServiceImp{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PostRepository postRepository;

    @Override
    public List<UserDTO> getAllUser() {
        List<Users> listUser = userRepository.findAll();
        List<UserDTO> userDTOlist = new ArrayList<>();

        for (Users user : listUser) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setFullName(user.getFullName());
            userDTO.setAvatar(user.getAvatar());
            userDTO.setDesc(user.getDesc());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setAddress(user.getAddress());
            userDTO.setDob(user.getDob());

            userDTOlist.add(userDTO);
        }

        return userDTOlist;
    }

    @Override
    public boolean addUser(SignUpRequest signUpRequest) {
        
        Users users = new Users();
        
        users.setEmail(signUpRequest.getEmail());
        users.setFullName(signUpRequest.getFullName());
        users.setAvatar(signUpRequest.getAvatar());
        users.setDesc(signUpRequest.getDesc());
        users.setPhoneNumber(signUpRequest.getPhoneNumber());
        users.setAddress(signUpRequest.getAddress());
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        users.setPassword(encodedPassword);

        try {
            userRepository.save(users);
            return true;
        } catch (Exception e) {
            return false;
        }
    
    }

    @Override
    public boolean checkLogin(String email, String password) {
        
       Users Users = userRepository.findByEmail(email);

       passwordEncoder.matches(password, Users.getPassword());

        return passwordEncoder.matches(password, Users.getPassword());
    }

    public Map<String, Long> getCount() {
        long userCount = userRepository.count(); // Số lượng người dùng
        long postCount = postRepository.count(); // Số lượng bài đăng

        Map<String, Long> countMap = new HashMap<>();
        countMap.put("userCount", userCount);
        countMap.put("postCount", postCount);

        return countMap;
    }
    
    
}
