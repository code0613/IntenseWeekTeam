package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.CommentDto;
import com.sparta.hanghaememo.dto.CommentResponseDto;
import com.sparta.hanghaememo.dto.ResponseMsgDto;
import com.sparta.hanghaememo.entity.*;
import com.sparta.hanghaememo.exception.ErrorCode;
import com.sparta.hanghaememo.exception.RequestException;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.BoardRepository;
import com.sparta.hanghaememo.repository.CommentLikeRepository;
import com.sparta.hanghaememo.repository.CommentRepository;
import com.sparta.hanghaememo.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {


    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    private final CommentLikeRepository commentLikeRepository;
    public CommentResponseDto addComment(CommentDto commentDto, Long id, User user) {


        Board board;
        board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode. NULL_CONTENTS_400)
        );
        Comment comment = commentRepository.save(new Comment(commentDto, user, board));

        return new CommentResponseDto(comment);

    }

    @Transactional  //객체가 변화가 있을 때 변화를 감지한다.
    public CommentResponseDto updateComment(CommentDto commentDto, Long id, User user) {

        Comment comment;

        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(id).orElseThrow(
                    () -> new RequestException(ErrorCode. NULL_COMMENT_400)
            );
        } else {
            comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new RequestException(ErrorCode. NULL_USER_400)
            );
        }
        comment.update(commentDto);

        return new CommentResponseDto(comment);

    }

    public void deleteComment(Long id, User user) {

        Comment comment;
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(id).orElseThrow(
                    () -> new RequestException(ErrorCode. NULL_COMMENT_400)

            );
        } else {
            comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new RequestException(ErrorCode. NULL_USER_400)
            );
        }
        commentRepository.delete(comment);
    }
    @Transactional
    public ResponseMsgDto commentLike(Long id, User user){
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()->new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        if(commentLikeRepository.findByCommentAndUserId(comment,user.getId()).isEmpty()){
            CommentLike commentLike = commentLikeRepository.save(new CommentLike(user,comment));
            comment.getCommentLikes().add(commentLike);

            /*comment.setCommentLikeCount(comment.getCommentLikeCount()+1);*/

            return new ResponseMsgDto(HttpStatus.OK.value(),"좋아요 성공");
        }else{
            commentLikeRepository.deleteByUserIdAndCommentId(user.getId(), comment.getId());
            /*comment.setCommentLikeCount(comment.getCommentLikeCount()+1);*/
            return new ResponseMsgDto(HttpStatus.OK.value(),"좋아요 취소");
        }
    }
}
