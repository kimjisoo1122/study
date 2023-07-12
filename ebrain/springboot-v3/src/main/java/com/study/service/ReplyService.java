package com.study.service;

import com.study.dto.ReplyDto;
import com.study.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글의 비즈니스로직을 처리하는 서비스 입니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyMapper replyMapper;

    /**
     * 게시글의 댓글을 등록한 후
     * 등록된 댓글을 조회하여 반환합니다.
     * @param replyDto
     * @return ReplyDto 등록된 댓글객체
     */
    public ReplyDto register(ReplyDto replyDto) {
        replyMapper.insert(replyDto);
        Long replyId = replyDto.getReplyId();
        return replyMapper.selectById(replyId);
    }

    /**
     * 댓글을 조회합니다.
     * @param replyId
     * @return ReplyDto
     */
    @Transactional(readOnly = true)
    public ReplyDto findById(Long replyId) {
        return replyMapper.selectById(replyId);
    }

    /**
     * 게시글번호로 댓글을 조회합니다.
     * @param boardId
     * @return List<ReplyDto>
     */
    @Transactional(readOnly = true)
    public List<ReplyDto> findByBoardId(Long boardId) {

        return replyMapper.selectByBoardId(boardId);
    }
}
