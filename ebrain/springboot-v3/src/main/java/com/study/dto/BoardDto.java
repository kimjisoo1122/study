package com.study.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private boolean hasFile; // 첨부파일 존재유무 (게시글 목록 Query에 사용)

    /**
     * 게시글에 맞는 포맷된 형식의 생성일시 반환합니다.
     * @return yyyy.MM.dd HH:mm 포맷의 생성일시
     */
    public String getFormattedCreateDate() {
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    /**
     * 게시글에 맞는 포맷된 형식의 수정일시를 반환합니다.
     * @return yyyy.MM.dd HH:mm 포맷의 수정일시
     */
    public String getFormattedUpdateDate() {
        return updateDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
