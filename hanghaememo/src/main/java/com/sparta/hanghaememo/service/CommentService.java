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

    private final JwtUtil jwtUtil;

    public CommentResponseDto addComment(CommentDto commentDto, Long id, User user) {
//        String token = jwtUtil.resolveToken(request);//검증받은 토큰으로
//        Claims claims;
//
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);  //토큰안에 있는 user 정보를 claims안에다가 넣어 놓은 상태
//            } else {
//                throw new RequestException(ErrorCode.BAD_TOKEN_400);
//            }
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(     // 검증받은 유저에 정보를 가져 오겠다.
//                    () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
//            );
//            Optional<Board> optionalBoard = memoRepository.findById(id);
//            Board memo = optionalBoard.orElseThrow(
//                    () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
//            );
//
////            Comment comment = Comment.builder()
////                    .commentId(commentDto.getCommentId())
////                    .memo(memo)
////                    .commentUsername(claims.getSubject())
////                    .commentContents(commentDto.getCommentContents())
////                    .build();
//
//            Comment comment = new Comment(commentDto, user, memo);
//
//            return new CommentResponseDto(commentRepository.save(comment));
//        }
//        throw new RequestException(ErrorCode.NULL_TOKEN_400);

        Board board;
        board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.save(new Comment(commentDto, user, board));

        return new CommentResponseDto(comment);

    }
    /*public CommentResponseDto getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("그 댓글은 존재하지 않습니다")
        );
        return new CommentResponseDto(comment);
    }*/


    @Transactional  //객체가 변화가 있을 때 변화를 감지한다.
    public CommentResponseDto updateComment(CommentDto commentDto, Long id, User user) {

        Comment comment;

        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("그 댓글은 존재하지 않습니다.")
            );
        } else {
            comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다")
            );
        }
        comment.update(commentDto);

        return new CommentResponseDto(comment);

    }

    public void deleteComment(Long id, User user) {

        Comment comment;
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")

            );
        } else {
            comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
        }
        commentRepository.delete(comment);
    }
    @Transactional
    public ResponseMsgDto commentLike(Long id, User user){
        Comment comment = commentRepository.findById(id).orElseThrow();

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
