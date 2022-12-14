package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.dto.BoardResponseDto;
import com.sparta.hanghaememo.dto.ResponseMsgDto;
import com.sparta.hanghaememo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController  //Json형태로 객체 데이터 반환
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/post")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.createBoard(requestDto, request);
    }

    @GetMapping("/posts")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();

    }

    @GetMapping("/post/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PutMapping("/post/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.update(id, requestDto, request);

    }

    @DeleteMapping("/post/{id}")   //
    public ResponseMsgDto deleteBoard(@PathVariable Long id,HttpServletRequest request){
        boardService.deleteBoard(id,request);
        return new ResponseMsgDto("삭제 완료", HttpStatus.OK.value());
    }
}