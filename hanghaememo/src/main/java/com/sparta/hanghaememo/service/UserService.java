package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.LoginRequestDto;
import com.sparta.hanghaememo.dto.SignupRequestDto;
import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.entity.UserRoleEnum;
import com.sparta.hanghaememo.exception.ErrorCode;
import com.sparta.hanghaememo.exception.RequestException;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; //의존성 주입

    // ADMIN_TOKEN
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    @Transactional
    public void signup(@Valid SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());   //저장하기 전에 password 를 Encoder 한다
//        String password = signupRequestDto.getPassword();

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new RequestException(ErrorCode.USER_OVERLAP_400);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new RequestException(ErrorCode.NULL_ADMIN_PASSWORD_400);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password,  role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_USER_400)
        );


        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw  new RequestException(ErrorCode.PASSWORD_NOT_400);
        }

//        // 비밀번호 확인
//        if(!user.getPassword().equals(password)){
//            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }

}