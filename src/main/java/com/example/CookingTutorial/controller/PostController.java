package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.request.PostCreateRequest;
import com.example.CookingTutorial.dto.response.Response;
import com.example.CookingTutorial.entity.*;
import com.example.CookingTutorial.repository.DislikeRepository;
import com.example.CookingTutorial.repository.LikeRepository;
import com.example.CookingTutorial.service.PostService;
import com.example.CookingTutorial.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DislikeRepository dislikeRepository;


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

    @GetMapping("/getPostByTypePost/{typePost}")
    public Response<?> getPostByType(@PathVariable("typePost") String typePost){

        if(!postService.getPostByType(typePost).isEmpty()){
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Get post successfully!")
                    .data(postService.getPostByType(typePost))
                    .build();
        }
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get post fail!")
                .build();

    }
    // Admin có thể xóa tất cả các bài viết, chỉ cần truyền ID bài viết vào
    @DeleteMapping("/deletePost/{postId}")
    public Response<?> deletePost(@PathVariable("postId") String postId) {
        boolean isDeleted = postService.deletePostByAdmin(postId);
        if (isDeleted) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Post deleted successfully.")
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to delete post.")
                    .build();
        }
    }

    @DeleteMapping("/deletePostMe/{postId}")
    public Response<?> deletePostMe(@PathVariable("postId") String postId) {
        boolean isDeleted = postService.deletePost(postId);
        if (isDeleted) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Post deleted successfully.")
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to delete post.")
                    .build();
        }
    }

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
                    .message("User not found!")
                    .build();
        }

        Post post = postService.getPost(postId);
        if (post == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Post not found!")
                    .build();
        }

        // Kiểm tra nếu người dùng đã like thì hủy like
        Optional<LikePost> existingLike =   likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent() ) {
            LikePost likePost = existingLike.get();
            likeRepository.delete(likePost);

            post.setLikeCount(post.getLikeCount() - 1);
            postService.updatePost(post);
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("You have unliked this post!")
                    .data(post)
                    .build();
        }

        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(post);
        likeRepository.save(likePost);

        post.setLikeCount(post.getLikeCount() + 1);
        postService.updatePost(post);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Liked the successful!")
                .data(post)
                .build();
    }

    @PostMapping("/dislike/{post_id}")
    public Response<?> dislikePost(@PathVariable("post_id") String postId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);
        if (user == null) {
            return Response.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("User not fuond!")
                    .build();
        }

        Post post = postService.getPost(postId);
        if (post == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Post not found!")
                    .build();
        }

        // Kiểm tra nếu người dùng đã dislike bài viết
        Optional<DislikePost> existingDislike = dislikeRepository.findByUserAndPost(user, post);
        if (existingDislike.isPresent()) {
            dislikeRepository.delete(existingDislike.get());
            post.setDislikeCount(post.getDislikeCount() - 1);

            postService.updatePost(post);
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Cancel dislike successfully!")
                    .data(post)
                    .build();
        }

        DislikePost dislikePost = new DislikePost();
        dislikePost.setUser(user);
        dislikePost.setPost(post);
        dislikeRepository.save(dislikePost);

        post.setDislikeCount(post.getDislikeCount() + 1);
        postService.updatePost(post);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Dislike post successfully!")
                .data(post)
                .build();
    }


    @GetMapping("/topDislikePost")
    public Response<?> getTopDislikePosts(@RequestParam("limit") int limit) {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get dislikepost successfully!")
                .data(postService.getTopPostsByDislike(limit)).build();
    }

    //lấy bài viết có lượng dislike cao nhất
    @GetMapping("/topLikePost")
    public Response<?> getTopLikePosts(@RequestParam("limit") int limit) {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get dislikepost successfully!")
                .data(postService.getTopPostsByLike(limit)).build();
    }

    // comment post
    @PostMapping("/comment/{post_id}")
    public Response<?> addComment(@PathVariable("post_id") String postId, @RequestBody String content) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);
        if (user == null) {
            return Response.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("User not found!")
                    .build();
        }

        Post post = postService.getPost(postId);
        if (post == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Post not found!")
                    .build();
        }

        CommentPost commentPost = CommentPost.builder()
                .content(content)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .post(post)
                .user(user)
                .build();

        post.getCommentPosts().add(commentPost);
        postService.savePost(post);

        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("add comment successfully!")
                .data(commentPost)
                .build();
    }


    @GetMapping("/comment/{post_id}")
    public Response<?> getComments(@PathVariable("post_id") String postId) {
        Post post = postService.getPost(postId);
        if (post == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Post not found!")
                    .build();
        }

        List<CommentPost> comments = postService.getCommentsByPost(post);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get comment of post have id"+ postId +" successfully!")
                .data(comments)
                .build();
    }

//    tạo mới một bài post
    @PostMapping("/createPost")
    public Response<?> createPost(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("tutorial") String tutorial,
            @RequestPart("typePost") String typePost,
            @RequestPart("images") MultipartFile[] files,
            @RequestPart("fileVideo") MultipartFile fileVideo,
            @RequestPart("ingredient") String ingredient
    ) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);
        if (user == null) {
            return Response.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("User not found!")
                    .build();
        }


        PostCreateRequest postRequest = new PostCreateRequest();
        postRequest.setDescription(description);
        postRequest.setTypePost(typePost);
        postRequest.setTitle(title);
        postRequest.setTutorial(tutorial);
        postRequest.setIngredients(ingredient);

        Post post = postService.createPost(postRequest, files, user, fileVideo);

        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Create post successfully!")
                .data(post)
                .build();
    }

    // admin duyệt bai đăng
    @PutMapping("/status/{post_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> statusPost(@PathVariable("post_id") String postId) {
        boolean isApproved = postService.statusPost(postId);
        if (isApproved) {
            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Post approved successfully!")
                    .build();
        } else {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Post not found!")
                    .build();
        }
    }

    @GetMapping("/getAllPost")
    public Response<?> getAllPost(){
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get all post successfully!")
                .data(postService.getAllPost())
                .build();
    }
    @GetMapping("/getAllPostWasApproved")
    public Response<?> getAllPostWasApproved(){
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get all post was approved successfully!")
                .data(postService.getAllPostApproved())
                .build();
    }


    @GetMapping("/unapproved")
    public Response<?> getUnapprovedPosts() {
        List<Post> unapprovedPosts = postService.getAllPostUnapprovedPosts();
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Get unapproved posts successfully!")
                .data(unapprovedPosts)
                .build();
    }

}
