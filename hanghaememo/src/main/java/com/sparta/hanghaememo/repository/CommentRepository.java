package com.sparta.hanghaememo.repository;


import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard(Board board);

    Optional<Comment> findByIdAndUserId(Long id, Long userId);
}