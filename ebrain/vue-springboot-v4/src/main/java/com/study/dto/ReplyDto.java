package com.study.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 댓글 DTO 입니다.
 */
@Data
public class ReplyDto {

    private Long replyId; // 댓글번호
    private Long boardId; // 게시글번호
    private String replyContent; // 댓글내용
    private LocalDateTime createDate; // 생성일시
    private LocalDateTime updateDate; // 수정일시
}