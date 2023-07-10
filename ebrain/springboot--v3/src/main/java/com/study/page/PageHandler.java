package com.study.page;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PageHandler {

    private int page;
    private int navSize = 10;
    private int pageSize;
    private int beginPage;
    private int endPage;
    private int maxPage;
    private int totalCnt;
    private boolean isPrevious;
    private boolean isNext;

    public PageHandler(int page, int totalCnt) {
        this(page, totalCnt, 10);
    }

    public PageHandler(int page, int totalCnt, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalCnt = totalCnt;

        maxPage = (int) Math.ceil((double) totalCnt / pageSize);
        beginPage = (int) ((double) (page - 1) / pageSize) * pageSize + 1;
        endPage = Math.min((beginPage + pageSize - 1), maxPage);
        isPrevious = beginPage > 1;
        isNext = maxPage != endPage;
    }
}
