package com.study.mapper;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;

import java.util.List;

public interface BoardMapper {

    int insert(BoardDto boardDto);
    List<BoardDto> findAllByCondition(BoardSearchCondition condition);
    int countByCondition(BoardSearchCondition condition);
    BoardDto findById(Long boardId);
    int update(BoardDto boardDto);

    void increaseViewCnt();

    int delete(Long boardId);




}
