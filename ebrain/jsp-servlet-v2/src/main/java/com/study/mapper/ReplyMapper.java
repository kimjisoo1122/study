package com.study.mapper;

import com.study.dto.ReplyDto;

import java.util.List;

public interface ReplyMapper {

    Long insert(ReplyDto replyDto);

    ReplyDto findById(Long replyId);

    List<ReplyDto> findByBoardId(Long boardId);
}
