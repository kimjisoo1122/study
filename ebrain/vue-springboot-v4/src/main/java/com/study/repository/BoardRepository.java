package com.study.repository;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final BoardMapper boardMapper;

    public int insert(BoardDto boardDto){
        return boardMapper.insert(boardDto);
    }

    public List<BoardDto> selectByCondition(BoardSearchCondition condition) {
        return boardMapper.selectByCondition(condition);
    }

    public int countByCondition(BoardSearchCondition condition) {
        return boardMapper.countByCondition(condition);
    }

    public BoardDto selectByBoardId(Long boardId) {
        return boardMapper.selectByBoardId(boardId);
    }

    public int update(BoardDto boardDto) {
        return boardMapper.update(boardDto);
    }

    public void increaseViewCnt(Long boardId) {
        boardMapper.increaseViewCnt(boardId);
    }

    public int delete(Long boardId) {
        return boardMapper.delete(boardId);
    }
}
