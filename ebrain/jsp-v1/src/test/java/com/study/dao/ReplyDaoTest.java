package com.study.dao;

import com.study.dto.BoardDto;
import com.study.dto.ReplyDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplyDaoTest {

    @Test
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

        // 댓글 등록
        ReplyDto replyDto = new ReplyDto();
        replyDto.setBoardId(boardId);
        replyDto.setContent("댓글 내용");

        ReplyDao replyDao = new ReplyDao();
        Long replyId = replyDao.register(replyDto);

        assertNotNull(replyId);

        // 댓글 조회
        ReplyDto reply = replyDao.findById(replyId);
        assertEquals(replyDto.getContent(), reply.getContent());

        // 게시글로 조회
        List<ReplyDto> replies = replyDao.findByBoardId(boardId);
        assertEquals(1, replies.size());


        // 댓글 롤백
        int deleteReplyCnt = replyDao.delete(replyId);
        assertEquals(1, deleteReplyCnt);

        // 게시글 롤백
        int deletedBoardCnt = boardDao.delete(boardId);
        assertEquals(1, deletedBoardCnt);
    }
}