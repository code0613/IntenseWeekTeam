package com.sparta.hanghaememo.dto;


import com.sparta.hanghaememo.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor

public class BoardResponseDto {
    private Long id;
    private String username;
    private String content;
    private String title;
    private List<CommentResponseDto> commentList;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.username = board.getUsername();
        this.content = board.getContent();
        this.title = board.getTitle();
        }

    public BoardResponseDto(Board board, List<CommentResponseDto> commentList) {      //매개변수를 가지는 생성자
        this.id = board.getId();            //this.id: (위에서 선언된) 필드, Board 객체의 board 매개변수로 들어온 데이터를 getId() 에 담는다(Client 에게로 보내기 위해)
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getUsername();
        this.commentList = commentList;

    }

}