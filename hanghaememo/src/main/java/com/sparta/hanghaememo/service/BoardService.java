package com.sparta.hanghaememo.service;


import com.sparta.hanghaememo.dto.*;
import com.sparta.hanghaememo.entity.*;
import com.sparta.hanghaememo.exception.ErrorCode;
import com.sparta.hanghaememo.exception.RequestException;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    private final CommentLikeRepository commentLikeRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final JwtUtil jwtUtil;


    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {

        Board board = boardRepository.save(new Board(requestDto, user));
        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
        List<BoardResponseDto> boardResponseDto = new ArrayList<>();

        for (Board board : boardList){
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
            for (Comment comment : board.getCommentList()) {
                commentResponseDtoList.add(new CommentResponseDto(comment,commentLikeRepository.countAllByCommentId(comment.getId()))); //좋아요 개수 확인
            }
            boardResponseDto.add(new BoardResponseDto(board, commentResponseDtoList));
        }
        return boardResponseDto;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard (Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : board.getCommentList()){
            commentResponseDtoList.add(new CommentResponseDto(comment,commentLikeRepository.countAllByCommentId(comment.getId())));
        }
        return new BoardResponseDto(board,commentResponseDtoList);
    }

    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto requestDto, User user) {
       Board board;
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            board = boardRepository.findById(id).orElseThrow(
                    () ->  new RequestException(ErrorCode.NULL_CONTENTS_400)

            );
        }else {
            board = (Board) boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () ->  new RequestException(ErrorCode.NULL_USER_400)
            );
        }
        board.update(requestDto);

        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long id,User user) {
        Board board;    //board 를 사용하기위해서는 이런 변수 선언 필요함

        //user 의 권한이 ADMIN 와 같다면,
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            board = boardRepository.findById(id).orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
                    //() -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400)
            );

        } else {
            //user 의 권한이 ADMIN 이 아니라면, 아이디가 같은 유저만 수정 가능
            board = (Board) boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_COMMENT_400)
            );
        }
        boardRepository.delete(board);
    }

    @Transactional
    public ResponseMsgDto boardLike(Long id, User user){
        Board board = boardRepository.findById(id).orElseThrow(
                ()->new RequestException(ErrorCode.NULL_CONTENTS_400)
        );
        if(boardLikeRepository.findByBoardAndUserId(board, user.getId()).isEmpty()){ // 보드라이크에 값이 있는지 확인
            boardLikeRepository.save(new BoardLike(user, board)); // 없으면 저장
            return new ResponseMsgDto(HttpStatus.OK.value(),"좋아요 성공");
        }
        else {
            boardLikeRepository.deleteByBoardIdAndUserId(board.getId(),user.getId());// 있으면 삭제
            return new ResponseMsgDto(HttpStatus.OK.value(),"좋아요 취소");
        }

    }
}