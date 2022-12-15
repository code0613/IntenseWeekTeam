package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.CommentDto;
import com.sparta.hanghaememo.dto.CommentResponseDto;
import com.sparta.hanghaememo.dto.ResponseMsgDto;
import com.sparta.hanghaememo.security.UserDetailsImpl;
import com.sparta.hanghaememo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    // 댓글 추가
    @PostMapping("/comment/{id}")
    public CommentResponseDto addComment(@RequestBody CommentDto commentDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComment(commentDto, id, userDetails.getUser());
    }


    // 댓글 수정
    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@RequestBody CommentDto commentDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentDto, id, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<ResponseMsgDto> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK.value(),"삭제 성공"));
    }
    @PostMapping("/comment/like/{id}")
    public ResponseMsgDto commentLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.commentLike(id, userDetails.getUser());
    }
}