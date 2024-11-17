package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.response.Response;
import com.example.CookingTutorial.entity.LikePost;
import com.example.CookingTutorial.entity.Post;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.repository.LikeRepository;
import com.example.CookingTutorial.service.PostService;
import com.example.CookingTutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/{post_id}")
    public Response<?> getPost(@PathVariable("post_id") String post_id) {
        Post post = postService.getPost(post_id);
        if (post == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Post not found")
                    .build();
        }
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get post successfully!")
                .data(post)
                .build();
    }

    // Tìm kiếm bài viết theo tiêu đề
    @GetMapping("/search")
    public Response<?> searchPostsByTitle(@RequestParam("title") String title) {
        List<Post> posts = postService.searchPostsByTitle(title);
        if (posts.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("No posts found with the given title")
                    .build();
        }
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Posts found successfully")
                .data(posts)
                .build();
    }

    //like bài viết

    @PostMapping("/like/{post_id}")
    public Response<?> likePost(@PathVariable("post_id") String postId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);
        if (user == null) {
            return Response.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Người dùng không tìm thấy")
                    .build();
        }

        Post post = postService.getPost(postId);
        if (post == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Bài viết không tìm thấy")
                    .build();
        }

        // Kiểm tra nếu người dùng đã like thì hủy like
        Optional<LikePost> existingLike =   likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {

            LikePost likePost = existingLike.get();
            likeRepository.delete(likePost);

            post.setLikeCount(post.getLikeCount() - 1);
            postService.updatePost(post);
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Bạn đã hủy thích bài viết này ")
                    .data(post)
                    .build();
        }


        // Tạo mới quan hệ "like" giữa người dùng và bài viết
        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(post);
        likeRepository.save(likePost);

        // Tăng số lượng like của bài viết
        post.setLikeCount(post.getLikeCount() + 1);
        postService.updatePost(post);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Like bài viết thành công")
                .data(post)
                .build();
    }







}
