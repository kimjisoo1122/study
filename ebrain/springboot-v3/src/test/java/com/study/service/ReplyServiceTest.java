package com.study.service;

import com.study.dto.BoardDto;
import com.study.dto.ReplyDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    BoardService boardService;

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

        // 댓글 등록
        ReplyDto replyDto = new ReplyDto();
        replyDto.setBoardId(boardId);
        replyDto.setReplyContent("댓글내용");
        ReplyDto registeredReply = replyService.register(replyDto);
        assertNotNull(registeredReply);

        ReplyDto findReply = replyService.findById(registeredReply.getReplyId());
        assertEquals(replyDto.getReplyContent(), findReply.getReplyContent());
    }
}