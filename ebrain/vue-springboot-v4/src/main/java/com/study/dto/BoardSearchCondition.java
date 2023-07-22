package com.study.dto;

import lombok.Data;

/**
 * 게시글 검색 조건을 설정하는 클래스입니다.
 */
@Data
public class BoardSearchCondition {

    // 게시글 검색조건
    private String fromDate; // 작성일 이후
    private String toDate; // 작성일 이전
    private String search; // 검색어
    private String searchCategory; // 검색 카테고리

    // 페이징처리에 사용합니다.
    private int page; // 현재 페이지
    private int pageSize; // 페이지 크기

    // 조회 SQL에 사용합니다.
    private int offset; // SQL OFFSET
    private int limit; // SQL LIMIT

    /**
     * 검색조건 페이징처리를 위한 값을 설정합니다.
     * @param page 현재 페이지
     * @param pageSize 페이지 크기
     */
    public void setPagination(int page, int pageSize) {
        this.page = Math.max(page, 1);
        this.pageSize = Math.max(pageSize, 10);
        this.offset = (page - 1) * pageSize;
        this.limit = pageSize;
    }
}