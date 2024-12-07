package com.example.CookingTutorial.service;

import com.example.CookingTutorial.dto.request.PostCreateRequest;
import com.example.CookingTutorial.entity.CommentPost;
import com.example.CookingTutorial.entity.Picture;
import com.example.CookingTutorial.entity.Post;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PictureReponsitory pictureRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public int numberOfPost(){
        List<Post> list=postRepository.findAll();
        return list.size();
    }


    public boolean deletePostByAdmin(String postId){

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }
        Post post = postOptional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Kiểm tra vai trò của người dùng
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));


        if (isAdmin || post.getUser().getEmail().equals(email)) {
            postRepository.deleteById(postId);
            return true;
        }

        return false;
    }
    public Post getPost(String post_id){
        return postRepository.findById(post_id).orElse(null);
    }


    public List<Post> getAllPost(){
        return  postRepository.findAll();
    }
    public List<Post> getPostByType(String typePost) {
        return postRepository.findPostByTypePost(typePost);
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


    public void savePost(Post post){
        postRepository.save(post);
    }

    // lấy danh sách post có dislike cao
    public List<Post> getTopPostsByDislike(int limit) {
        return postRepository.findTopPostsByDislikeCount(limit);
    }
    //create post
    public Post createPost(PostCreateRequest request, MultipartFile[] files, User user, MultipartFile fileVideo ) {

        Post post = Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .typePost(request.getTypePost())
                .tutorial(request.getTutorial())
                .user(user)
                .build();


        List<Picture> pictures = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String imageUrl = cloudinaryService.uploadImage(file); // Upload ảnh lên Cloudinary

            Picture picture = Picture.builder()
                    .link(imageUrl)
                    .alt("Step " + (i + 1) + " image for post " + post.getTitle())
                    .post(post)
                    .build();

            pictures.add(picture);

        }
        post.setLinkVideo(cloudinaryService.uploadVideo(fileVideo));

        post.setPictures(pictures);
        post = postRepository.save(post);
        pictureRepository.saveAll(pictures);

        return post;
    }

}