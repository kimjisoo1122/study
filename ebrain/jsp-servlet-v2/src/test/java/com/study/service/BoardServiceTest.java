package com.study.service;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    @Test
    void findAll() {
        BoardService boardService = new BoardService();
        BoardSearchCondition condition = new BoardSearchCondition();
//        condition.setSearchCategory("1");
        condition.setSearch("김지수");
        condition.setFromDate("2022-07-05");
        condition.setToDate("2023-07-05");
        List<BoardDto> boards = boardService.findAllByCondition(condition);
    }

}