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
    private Long mId;

    private String commentContents;

    public CommentResponseDto(Comment comment){

        this.commentId = comment.getCommentId();
        this.mId = comment.getBoard().getId();
        this.commentContents = comment.getCommentContents();
    }
}



