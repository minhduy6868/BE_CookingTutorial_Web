package com.example.tutorial.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.tutorial.Repository.UserRepository;
import com.example.tutorial.entity.Users;



@Service
public class CustomUserDetailService implements UserDetailsService  {
    
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        Users user = userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User can't exist");
        }
        return new User(email, user.getPassword(), new ArrayList<>());
    }
}
