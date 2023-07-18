package com.study.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 게시글 DTO 입니다.
 */
@Data
public class BoardDto {

    private Long boardId; // 게시글 번호
    private Long categoryId; // 카테고리 번호
    private String categoryName; // 카테고리 이름
    private String writer; // 작성자
    private String title; // 제목
    private String password; // 비밀번호
    private String content; // 내용
    private LocalDateTime createDate; // 생성일시
    private LocalDateTime updateDate; // 수정일시
    private int viewCnt; // 조회수
    private boolean hasFile; // 첨부파일 존재유무 (게시글 목록 쿼리에 사용)
}
