package com.study.service;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BoardServiceTest {

    static BoardService boardService = BoardService.getBoardService();
    @Test
    void register() {
        // 게시글 등록
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setWriter("테스트");
        boardDto.setTitle("테스트제목");
        boardDto.setContent("테스트내용");
        boardDto.setPassword("test1234!");
        Long boardId = boardService.register(boardDto);
        assertNotNull(boardId);

        // 게시글 롤백
        int deletedRow = boardService.delete(boardId);
        assertEquals(1, deletedRow);
    }


    @Test
    void find() {
        // 게시글 등록
        String uuid = UUID.randomUUID().toString();

        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setWriter(uuid);
        boardDto.setTitle(uuid);
        boardDto.setContent(uuid);
        boardDto.setPassword("test1234!");
        Long boardId = boardService.register(boardDto);

        assertNotNull(boardId);

        // 게시글 조회
        BoardDto findBoard = boardService.findById(boardId);

        assertNotNull(findBoard);

        // 검색조건
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setSearchCategory("1");
        condition.setSearch(uuid);
        condition.setOffset(0);
        condition.setLimit(1);
        LocalDate now = LocalDate.now();
        condition.setFromDate(now.minusYears(1).format(DateTimeFormatter.ISO_DATE));
        condition.setToDate(now.format(DateTimeFormatter.ISO_DATE));

        List<BoardDto> boardsByCondition = boardService.findByCondition(condition);

        assertEquals(1, boardsByCondition.size());

        // 게시글 롤백
        int deletedRow = boardService.delete(boardId);
        assertEquals(1, deletedRow);
    }

    @Test
    void update() {
        // 게시글 등록
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setWriter("테스트");
        boardDto.setTitle("테스트제목");
        boardDto.setContent("테스트내용");
        boardDto.setPassword("test1234!");
        Long boardId = boardService.register(boardDto);

        assertNotNull(boardId);

        BoardDto updateBoard = new BoardDto();
        updateBoard.setBoardId(boardId);
        updateBoard.setWriter("수정자");
        updateBoard.setTitle("수정제목");
        updateBoard.setContent("수정내용");
        updateBoard.setPassword("test12345!");
        int updatedRow = boardService.update(updateBoard);
        BoardDto updatedBoard = boardService.findById(boardId);

        assertEquals(1, updatedRow);
        assertEquals(updateBoard.getWriter(), updatedBoard.getWriter());
        assertEquals(updateBoard.getTitle(), updatedBoard.getTitle());
        assertEquals(updateBoard.getContent(), updatedBoard.getContent());
        assertEquals(updateBoard.getPassword(), updatedBoard.getPassword());

        // 게시글 롤백
        int deletedRow = boardService.delete(boardId);
        assertEquals(1, deletedRow);
    }

    @Test
    void increaseViewCnt() {
        // 게시글 등록
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setWriter("테스트");
        boardDto.setTitle("테스트제목");
        boardDto.setContent("테스트내용");
        boardDto.setPassword("test1234!");
        Long boardId = boardService.register(boardDto);

        assertNotNull(boardId);

        boardService.increaseViewCnt();
        BoardDto findBoard = boardService.findById(boardId);

        assertEquals(1, findBoard.getViewCnt());

        // 게시글 롤백
        int deletedRow = boardService.delete(boardId);
        assertEquals(1, deletedRow);
    }
}