package com.study.mapper;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;

import java.util.List;

public interface BoardMapper {
    List<BoardDto> findAllByCondition(BoardSearchCondition condition);
}
