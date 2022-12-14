package com.sparta.hanghaememo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.hanghaememo.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "comment",cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @Column(nullable = false)
    private String username;
    @JsonIgnore  //??@JsonIgnore: Response에 해당 필드가 제외된다
    @ManyToOne(fetch = FetchType.LAZY)   //?
//    @JoinColumn(name = "Board_Id")   //외래 키 매핑 시 사용 (name = 매핑할 외래키 이름)
    private Board board;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(nullable = false)
    private String commentContents;



    public Comment (CommentDto commentDto, User user, Board board) {
        this.username = user.getUsername();
        this.commentContents = commentDto.getCommentContents();
        this.board = board;
        this.user = user;
    }

    public void update(CommentDto commentDto) {
        this.commentContents = commentDto.getCommentContents();
    }
}