package com.example.CookingTutorial.service;

import com.example.CookingTutorial.entity.CommentPost;
import com.example.CookingTutorial.entity.Post;
import com.example.CookingTutorial.repository.CommentRepository;
import com.example.CookingTutorial.repository.LikeRepository;
import com.example.CookingTutorial.repository.PostRepository;
import com.example.CookingTutorial.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;
    public int numberOfPost(){
        List<Post> list=postRepository.findAll();
        return list.size();
    }

    public boolean deletePost(String postId){
        if(postRepository.findById(postId).isEmpty()){
            return false;
        }
        postRepository.deleteById(postId);
        return true;
    }
    public Post getPost(String post_id){
        return postRepository.findById(post_id).orElse(null);
    }

    public List<Post> getAllPost(){
        return  postRepository.findAll();
    }

    public List<Post> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }


    public void updatePost(Post post) {
        postRepository.save(post);
    }



    // comment post
    public void saveComment(CommentPost comment) {
        commentRepository.save(comment);
    }

    public List<CommentPost> getCommentsByPost(Post post) {
        return commentRepository.findByPost(post);
    }
}
