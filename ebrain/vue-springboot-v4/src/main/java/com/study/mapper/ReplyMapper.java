package com.study.mapper;

import com.study.dto.ReplyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyMapper {

    Long insert(ReplyDto replyDto);
    ReplyDto selectByReplyId(Long replyId);
    List<ReplyDto> selectByBoardId(Long boardId);
    int deleteByBoardId(Long boardId);
}
