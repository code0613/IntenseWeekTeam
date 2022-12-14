package com.sparta.hanghaememo.repository;

import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    Optional<BoardLike> findByBoardAndUserId(Board board ,Long userid);

    void deleteByBoardIdAndUserId(Long boardid, Long userid);
}
