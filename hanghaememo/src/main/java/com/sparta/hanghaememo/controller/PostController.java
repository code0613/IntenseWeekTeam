package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.PostRequestDto;
import com.sparta.hanghaememo.dto.PostResponseDto;
import com.sparta.hanghaememo.dto.ResponseMsgDto;
import com.sparta.hanghaememo.entity.Post;
import com.sparta.hanghaememo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController  //Json형태로 객체 데이터 반환
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();

    }

    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.update(id, requestDto, request);

    }

    @DeleteMapping("/post/{id}")   //
    public ResponseMsgDto deletePost(@PathVariable Long id,HttpServletRequest request){
        postService.deletePost(id,request);
        return new ResponseMsgDto("삭제 완료", HttpStatus.OK.value());
    }
}