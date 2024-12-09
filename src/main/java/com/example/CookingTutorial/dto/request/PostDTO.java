package com.example.CookingTutorial.dto.request;

import com.example.CookingTutorial.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    String id;
    String linkVideo;
    String title;
    String description;
    String tutorial;
    String typePost;
    int likeCount;
    int dislikeCount;

    private boolean isApproved;
    List<LikePost> likePosts;
    List<DislikePost> dislikePosts;
    List<Ingredient> ingredients;
    List<CommentPost> commentPosts;
    List<PictureDTO> pictures;
    User user;
}