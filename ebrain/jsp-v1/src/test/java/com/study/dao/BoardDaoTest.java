package com.study.dao;

import com.study.dto.BoardDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class BoardDaoTest {

    @Test
    void register() {
        BoardDto boardDto =
                new BoardDto(1L, "제목",
                        "작성자", "내용", "비밀번호");
        BoardDao boardDao = new BoardDao();
        long boardId = boardDao.register(boardDto);

        int deletedRowCnt = boardDao.delete(boardId);

        assertEquals(1, deletedRowCnt);
    }
}