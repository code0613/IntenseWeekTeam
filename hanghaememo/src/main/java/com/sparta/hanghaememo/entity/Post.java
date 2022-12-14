package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;



@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long userId;
    public Post(PostRequestDto requestDto, Long userid, String username) {
        this.username = username;
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.userId = userid;

        }

    public void update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();


    }
}
