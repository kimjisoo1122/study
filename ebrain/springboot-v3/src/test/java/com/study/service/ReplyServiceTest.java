package com.study.service;

import com.study.dto.BoardRegisterForm;
import com.study.dto.ReplyDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    BoardService boardService;

    @Test
    void register() throws IOException {
        // 게시글 등록
        BoardRegisterForm registerForm = new BoardRegisterForm();
        registerForm.setCategoryId(1L);
        registerForm.setWriter("테스트");
        registerForm.setTitle("테스트제목");
        registerForm.setContent("테스트내용");
        registerForm.setPassword("test1234!");
        Long boardId = boardService.register(registerForm);

        assertNotNull(boardId);

        // 댓글 등록
        ReplyDto registerReply = new ReplyDto();
        registerReply.setBoardId(boardId);
        registerReply.setReplyContent("댓글내용");
        ReplyDto registeredReply = replyService.register(registerReply);

        assertNotNull(registeredReply);

        ReplyDto findReply = replyService.findById(registeredReply.getReplyId());

        assertNotNull(findReply);
        assertEquals(registerReply.getReplyContent(), findReply.getReplyContent());
    }
}