package com.example.CookingTutorial.service;

import com.example.CookingTutorial.entity.Post;
import com.example.CookingTutorial.reponsitory.PostReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PostService {
    @Autowired
    private PostReponsitory postReponsitory;

    public int numberOfPost(){
        List<Post> list=postReponsitory.findAll();
        return list.size();
    }

    public boolean deletePost(String postId){
        if(postReponsitory.findById(postId).isEmpty()){
            return false;
        }
        postReponsitory.deleteById(postId);
        return true;
    }
}
