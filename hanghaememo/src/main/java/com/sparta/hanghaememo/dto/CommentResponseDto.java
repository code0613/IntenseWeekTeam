package com.sparta.hanghaememo.dto;

import com.sparta.hanghaememo.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String commentContents;
    private int commentLikeCount;

    public CommentResponseDto(Comment comment){

        this.commentId = comment.getId();

        this.commentContents = comment.getCommentContents();
    }

    public CommentResponseDto(Comment comment, int commentLike){

        this.commentId= comment.getId();
        this.commentContents = comment.getCommentContents();
        this.commentLikeCount = commentLike;

    }
}



