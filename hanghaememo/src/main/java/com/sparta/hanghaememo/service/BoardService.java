package com.sparta.hanghaememo.service;


import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.dto.BoardResponseDto;
import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.BoardRepository;
import com.sparta.hanghaememo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException(("Token Error"));

            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
            );


            Board board = boardRepository.save(new Board(requestDto, user.getId(), user.getUsername()));
            return new BoardResponseDto(board);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
        List<BoardResponseDto> boardResponseDto = new ArrayList<>();
        for (Board board : boardList){
            BoardResponseDto boardDto = new BoardResponseDto(board);
            boardResponseDto.add(boardDto);

        }


        return boardResponseDto;
    }

    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException(("Token Error"));

            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
            );


            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.."));
            board.update(requestDto);
            BoardResponseDto boardResponseDto = new BoardResponseDto(board);
            return boardResponseDto;
        } else {
            return null;
        }

    }

    @Transactional
    public void deleteBoard(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException(("Token Error"));

            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
            );

            Board board = (Board) boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new NullPointerException("아이디가 일치하지 않습니다.")
            );
            boardRepository.delete(board);

        }
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard (Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("게시글을 찾을 수 없다.")
        );
        return new BoardResponseDto(board);
    }
}