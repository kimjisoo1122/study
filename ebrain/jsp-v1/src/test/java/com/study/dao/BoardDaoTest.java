package com.study.dao;

import com.study.dto.BoardDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class BoardDaoTest {

    @Test
    @DisplayName("게시글 등록")
    void register() {
        // 게시글 등록
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setTitle("제목");
        boardDto.setWriter("작성자");
        boardDto.setContent("내용");
        boardDto.setPassword("비밀번호");
        BoardDao boardDao = new BoardDao();
        long boardId = boardDao.register(boardDto);

        // 게시글 롤백
        int deletedRowCnt = boardDao.delete(boardId);

        assertEquals(1, deletedRowCnt);
    }

    @Test
    @DisplayName("게시글 조회")
    void findById() {
        // 게시글 등록
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setTitle("제목");
        boardDto.setWriter("작성자");
        boardDto.setContent("내용");
        boardDto.setPassword("비밀번호");
        BoardDao boardDao = new BoardDao();
        long boardId = boardDao.register(boardDto);

        // 게시글 조회
        BoardDto board = boardDao.findById(boardId);

        assertEquals(boardDto.getTitle(), board.getTitle());
        assertEquals(boardDto.getWriter(), board.getWriter());
        assertEquals(boardDto.getContent(), board.getContent());
        assertEquals(boardDto.getPassword(), board.getPassword());
        assertEquals(0, board.getViewCnt());
        assertEquals("JAVA", board.getCategoryName());

        // 게시글 롤백
        int deletedRowCnt = boardDao.delete(boardId);

        assertEquals(1, deletedRowCnt);
    }
}