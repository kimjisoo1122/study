package com.study.dto;

import com.study.util.StringUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class BoardSearchCondition {

    private String categoryId;
    private String search;
    private String fromDate;
    private String toDate;

    private int page;
    private int pageSize;
    private int offset;
    private int limit;

    /**
     * 클라이언트의 request를 파싱하여 검색조건을 설정한다.
     * @param request
     */
    public void setConditionByReq(HttpServletRequest request) {
        fromDate = StringUtil.nvl(request.getParameter("fromDate")) ;
        toDate = StringUtil.nvl(request.getParameter("toDate"));
        search = StringUtil.nvl(request.getParameter("search"));
        categoryId = StringUtil.nvl(request.getParameter("categoryId"));
        page = Integer.parseInt(StringUtil.nvl(request.getParameter("page"), "1"));
        pageSize = Integer.parseInt(StringUtil.nvl(request.getParameter("pageSize"), "10"));

        // 페이징 처리
        offset = (page - 1) * pageSize;
        limit = pageSize;
    }
}
