package com.study.service;

import com.study.dto.BoardDto;
import com.study.dto.BoardRegisterForm;
import com.study.dto.BoardSearchCondition;
import com.study.dto.BoardUpdateForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Test
    void register() throws Exception {
        // 게시글 등록
        BoardRegisterForm registerForm = new BoardRegisterForm();
        registerForm.setCategoryId(1L);
        registerForm.setWriter("테스트");
        registerForm.setTitle("테스트제목");
        registerForm.setContent("테스트내용");
        registerForm.setPassword("test1234!");
        Long boardId = boardService.register(registerForm);
        assertNotNull(boardId);
    }


    @Test
    void findByCondition() throws IOException {
        // 게시글 등록
        String uuid = UUID.randomUUID().toString();

        BoardRegisterForm registerForm = new BoardRegisterForm();
        registerForm.setCategoryId(1L);
        registerForm.setWriter(uuid);
        registerForm.setTitle(uuid);
        registerForm.setContent(uuid);
        registerForm.setPassword("test1234!");
        Long boardId = boardService.register(registerForm);

        assertNotNull(boardId);

        // 게시글 조회
        BoardDto findBoard = boardService.findById(boardId);

        assertNotNull(findBoard);

        // 검색조건으로 조회
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setSearchCategory(String.valueOf(registerForm.getCategoryId()));
        condition.setSearch(uuid);
        condition.setOffset(0);
        condition.setLimit(1);
        List<BoardDto> boards = boardService.findByCondition(condition);

        assertEquals(1, boards.size());
    }

    @Test
    void update() throws IOException {
        // 게시글 등록
        BoardRegisterForm registerForm = new BoardRegisterForm();
        registerForm.setCategoryId(1L);
        registerForm.setWriter("테스트");
        registerForm.setTitle("테스트제목");
        registerForm.setContent("테스트내용");
        registerForm.setPassword("test1234!");
        Long boardId = boardService.register(registerForm);

        assertNotNull(boardId);

        BoardUpdateForm updateForm = new BoardUpdateForm();
        updateForm.setBoardId(boardId);
        updateForm.setWriter("수정자");
        updateForm.setTitle("수정제목");
        updateForm.setContent("수정내용");
        int updatedRow = boardService.update(updateForm);

        assertEquals(1, updatedRow);

        // 게시글 조회
        BoardDto findBoard = boardService.findById(boardId);

        assertEquals(updateForm.getWriter(), findBoard.getWriter());
        assertEquals(updateForm.getTitle(), findBoard.getTitle());
        assertEquals(updateForm.getContent(), findBoard.getContent());
        assertEquals(boardService.encryptPwd(registerForm.getPassword()), findBoard.getPassword());
    }

    @Test
    void increaseViewCnt() throws IOException {
        // 게시글 등록
        BoardRegisterForm registerForm = new BoardRegisterForm();
        registerForm.setCategoryId(1L);
        registerForm.setWriter("테스트");
        registerForm.setTitle("테스트제목");
        registerForm.setContent("테스트내용");
        registerForm.setPassword("test1234!");
        Long boardId = boardService.register(registerForm);
        assertNotNull(boardId);

        boardService.increaseViewCnt(boardId);
        BoardDto findBoard = boardService.findById(boardId);

        assertEquals(1, findBoard.getViewCnt());
    }
}