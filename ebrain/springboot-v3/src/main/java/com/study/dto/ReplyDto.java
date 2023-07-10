package com.study.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ReplyDto {

    private Long replyId;
    private Long boardId;
    private String replyContent;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public String getFormattedCreateDate() {
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

}