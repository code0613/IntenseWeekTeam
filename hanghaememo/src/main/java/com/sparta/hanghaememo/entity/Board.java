package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;


    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();


    public Board(BoardRequestDto requestDto, User user) {
        this.username = user.getUsername();
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.user = user;

        }

    public void update(BoardRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();


    }
}
