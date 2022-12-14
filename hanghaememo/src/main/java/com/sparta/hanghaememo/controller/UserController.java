package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.LoginRequestDto;
import com.sparta.hanghaememo.dto.ResponseMsgDto;
import com.sparta.hanghaememo.dto.SignupRequestDto;
import com.sparta.hanghaememo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<ResponseMsgDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
//        return new ResponseMsgDto("회원가입 성공!", HttpStatus.OK.value());
        return ResponseEntity.ok(new ResponseMsgDto("회원가입 완료", HttpStatus.OK.value()));
    }


    @PostMapping("/login")
    public  ResponseEntity<ResponseMsgDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
//        return new ResponseMsgDto("로그인 성공!", HttpStatus.OK.value());
        return ResponseEntity.ok(new ResponseMsgDto("로그인 완료",HttpStatus.OK.value()));
    }



}