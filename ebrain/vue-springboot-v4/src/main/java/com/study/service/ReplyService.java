package com.study.service;

import com.study.dto.ReplyDto;
import com.study.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글의 비즈니스로직을 처리하는 서비스 입니다.
 */
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    /**
     * 게시글의 댓글을 등록한 후
     * 등록된 댓글을 조회하여 반환합니다.
     * @param replyDto 댓글정보 (boardId (게시글번호), replyContent (댓글내용)
     * @return ReplyDto 등록된 댓글객체
     */
    @Transactional
    public ReplyDto register(ReplyDto replyDto) {
        replyRepository.insert(replyDto);
        Long replyId = replyDto.getReplyId();
        return replyRepository.selectByReplyId(replyId);
    }

    /**
     * 댓글을 조회합니다.
     * @param replyId 댓글번호
     * @return ReplyDto 댓글정보
     */
    public ReplyDto findByReplyId(Long replyId) {
        return replyRepository.selectByReplyId(replyId);
    }

    /**
     * 게시글번호로 댓글을 조회합니다.
     * @param boardId 게시글 번호
     * @return List<ReplyDto> 게시글 댓글정보
     */
    public List<ReplyDto> findAllByBoardId(Long boardId) {
        return replyRepository.selectByBoardId(boardId);
    }
}
