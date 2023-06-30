package com.study.dao;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @Test
    void update() {
        // 게시글 등록
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setTitle("제목");
        boardDto.setWriter("작성자");
        boardDto.setContent("내용");
        boardDto.setPassword("비밀번호");
        BoardDao boardDao = new BoardDao();
        long boardId = boardDao.register(boardDto);

        Map<String, String> map = new HashMap<>();
        map.put("title", "수정제목");
        map.put("writer", "수정작성자");
        map.put("content", "수정내용");
        map.put("password", "수정비밀번호");
        map.put("view_cnt", "0");
        int updatedCnt = boardDao.update(map, boardId);
        assertEquals(1, updatedCnt);

        // 게시글 조회
        BoardDto board = boardDao.findById(boardId);
        assertEquals("수정제목", board.getTitle());
        assertEquals("수정작성자", board.getWriter());
        assertEquals("수정내용", board.getContent());
        assertEquals("수정비밀번호", board.getPassword());
        assertEquals(0, board.getViewCnt());
        assertEquals("JAVA", board.getCategoryName());


        // 게시글 롤백
        int deletedRowCnt = boardDao.delete(boardId);
        assertEquals(1, deletedRowCnt);
    }

    @Test
    void findAll() {
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
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setCategoryId(1L);
        condition.setSearch("작성자");
        condition.setOffset(0);
        condition.setLimit(10);
        List<BoardDto> all = boardDao.findAll(condition);
        assertNotEquals(0, all.size());

        // 게시글 롤백
        int deletedRowCnt = boardDao.delete(boardId);
        assertEquals(1, deletedRowCnt);
    }
}