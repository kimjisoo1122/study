package com.study.repository;

import com.study.dto.ReplyDto;
import com.study.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {

    private final ReplyMapper replyMapper;

    public Long insert(ReplyDto replyDto) {
        return replyMapper.insert(replyDto);
    }

    public ReplyDto selectByReplyId(Long replyId) {
        return replyMapper.selectByReplyId(replyId);
    }

    public List<ReplyDto> selectByBoardId(Long boardId) {
        return replyMapper.selectByBoardId(boardId);
    }

    public int deleteByBoardId(Long boardId) {
        return replyMapper.deleteByBoardId(boardId);
    }
}
