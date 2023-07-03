package com.study.page;

public class PageHandler {

    private int page;
    private int navSize = 10;
    private int pageSize;
    private int beginPage;
    private int endPage;
    private int maxPage;
    private boolean isPrevious;
    private boolean isNext;

    public PageHandler(int page, int totalCnt) {
        this(page, totalCnt, 10);
    }

    public PageHandler(int page, int totalCnt, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;

        maxPage = (int) Math.ceil((double) totalCnt / pageSize);
        beginPage = (int) ((double) (page - 1) / pageSize) * pageSize + 1;
        endPage = Math.min((beginPage + pageSize - 1), maxPage);
        isPrevious = beginPage > 1;
        isNext = maxPage != endPage;
    }

    public int getPage() {
        return page;
    }

    public int getNavSize() {
        return navSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public boolean isPrevious() {
        return isPrevious;
    }

    public boolean isNext() {
        return isNext;
    }
}
