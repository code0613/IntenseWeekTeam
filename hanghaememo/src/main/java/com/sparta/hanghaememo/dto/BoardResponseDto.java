package com.sparta.hanghaememo.dto;


import com.sparta.hanghaememo.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

public class BoardResponseDto {
    private Long id;
    private String username;
    private String content;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.username = board.getUsername();
        this.content = board.getContent();
        this.title = board.getTitle();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        }


}