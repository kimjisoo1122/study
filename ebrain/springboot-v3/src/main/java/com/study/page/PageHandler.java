package com.study.page;

import lombok.Data;

/**
 * 게시글의 페이지처리를 담당하는 핸들러 입니다.
 */
@Data
public class PageHandler {

    private int page; // 현재페이지
    private int navSize;// 네비게이션 사이즈
    private int pageSize; // 페이지 사이즈
    private int beginPage; // 시작페이지
    private int endPage; // 마지막페이지
    private int maxPage; // 게시글 총 페이지
    private int totalCnt; // 게시글 총 갯수
    private boolean isPrevious; // 이전페이지 유무
    private boolean isNext; // 다음페이지 유무

    public PageHandler(int page, int totalCnt) {
        this(page, totalCnt, 10);
    }

    public PageHandler(int page, int totalCnt, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalCnt = totalCnt;
        this.navSize = 10;

        maxPage = (int) Math.ceil((double) totalCnt / pageSize);
        beginPage = (int) ((double) (page - 1) / navSize) * navSize + 1;
        endPage = Math.min((beginPage + navSize - 1), maxPage);
        isPrevious = beginPage > 1;
        isNext = maxPage != endPage;
    }
}
