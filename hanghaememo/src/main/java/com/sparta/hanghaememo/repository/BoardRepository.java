package com.sparta.hanghaememo.repository;

import com.sparta.hanghaememo.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedAtDesc();

    Optional<Object> findByIdAndUserId(Long id, Long userId);
}