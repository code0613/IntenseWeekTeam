package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.CommentDto;
import com.sparta.hanghaememo.dto.CommentResponseDto;
import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.entity.Comment;
import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.exception.ErrorCode;
import com.sparta.hanghaememo.exception.RequestException;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.BoardRepository;
import com.sparta.hanghaememo.repository.CommentRepository;
import com.sparta.hanghaememo.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository memoRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CommentResponseDto addComment(CommentDto commentDto, Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);//검증받은 토큰으로
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);  //토큰안에 있는 user 정보를 claims안에다가 넣어 놓은 상태
            } else {
                throw new RequestException(ErrorCode.BAD_TOKEN_400);
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(     // 검증받은 유저에 정보를 가져 오겠다.
                    () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
            );
            Optional<Board> optionalBoard = memoRepository.findById(id);
            Board memo = optionalBoard.orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
            );

//            Comment comment = Comment.builder()
//                    .commentId(commentDto.getCommentId())
//                    .memo(memo)
//                    .commentUsername(claims.getSubject())
//                    .commentContents(commentDto.getCommentContents())
//                    .build();

            Comment comment = new Comment(commentDto, user, memo);

            return new CommentResponseDto(commentRepository.save(comment));
        }
        System.out.println("64 ");
        throw new RequestException(ErrorCode.NULL_TOKEN_400);
    }


    @Transactional  //객체가 변화가 있을 때 변화를 감지한다.
    public CommentResponseDto updateComment(CommentDto commentDto, Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        System.out.println("token = " + token);
        Claims claims;

        Optional<Comment> optionalCommnet = commentRepository.findById(id);
        Comment comment = optionalCommnet.orElseThrow(
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );
        comment.update(commentDto);
        return new CommentResponseDto(comment);



//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new RequestException(ErrorCode.BAD_TOKEN_400);
//            }
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new RequestException(ErrorCode.NULL_USER_400)
//            );
//
//            Optional<Comment> optionalCommnet = commentRepository.findById(id);
//            Comment comment = optionalCommnet.orElseThrow(
//                    () -> new RequestException(ErrorCode.NULL_COMMENT_400)
//            );
//            comment.update(commentDto);
//
//            if (comment.getUser().getUsername().equals(claims.getSubject())) {
//                comment.update(commentDto);
//               // return new CommentResponseDto(commentRepository.save(comment));
//            } else if (user.getRole() == UserRoleEnum.ADMIN) {
//                 comment.update(commentDto);
//               // return new CommentResponseDto(commentRepository.save(comment));
//            }else {
//                throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
//            }
//        }
//        System.out.println("104");
//        throw new RequestException(ErrorCode.NULL_TOKEN_400);
    }



    public void deleteComment(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        System.out.println("token = " + token);
        Claims claims;

        Optional<Comment> optionalCommnet = commentRepository.findById(id);
        Comment comment = optionalCommnet.orElseThrow(
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        commentRepository.delete(comment);

    }

//
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new RequestException(ErrorCode.BAD_TOKEN_400);
//            }
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new RequestException(ErrorCode.NULL_USER_400)
//            );
//
//            Optional<Comment> optionalCommnet = commentRepository.findById(id);
//            Comment comment = optionalCommnet.orElseThrow(
//                    () -> new RequestException(ErrorCode.NULL_COMMENT_400)
//            );
//
//            if (comment.getUser().getUsername().equals(claims.getSubject())) {
//                commentRepository.delete(comment);
////                return new CommentDto(comment);
//            }else if (user.getRole() == UserRoleEnum.ADMIN) {
//                commentRepository.delete(comment);
////                return new CommentDto(comment);
//            }else {
//                throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
//            }
//        }
//        System.out.println("140");
//        throw new RequestException(ErrorCode.NULL_TOKEN_400);
//    }
}
