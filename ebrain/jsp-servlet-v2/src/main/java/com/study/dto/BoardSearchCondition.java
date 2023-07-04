package com.study.dto;

import com.oreilly.servlet.MultipartRequest;
import com.study.util.StringUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.events.Characters;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.function.Function;

@Data
public class BoardSearchCondition {

    private String searchCategory;
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
    public void setConditionByReq(HttpServletRequest request) throws UnsupportedEncodingException {
        fromDate = StringUtil.nvl(request.getParameter("fromDate")) ;
        toDate = StringUtil.nvl(request.getParameter("toDate"));
        search = URLDecoder.decode(StringUtil.nvl(request.getParameter("search")), "UTF-8");
        searchCategory = StringUtil.nvl(request.getParameter("searchCategory"));
        page = Integer.parseInt(StringUtil.nvl(request.getParameter("page"), "1"));
        pageSize = Integer.parseInt(StringUtil.nvl(request.getParameter("pageSize"), "10"));

        // 페이징 처리
        offset = (page - 1) * pageSize;
        limit = pageSize;
    }

    public void setConditionByReq(MultipartRequest multi) throws UnsupportedEncodingException {
        fromDate = StringUtil.nvl(multi.getParameter("fromDate")) ;
        toDate = StringUtil.nvl(multi.getParameter("toDate"));
        search = URLDecoder.decode(StringUtil.nvl(multi.getParameter("search")), "UTF-8");
        searchCategory = StringUtil.nvl(multi.getParameter("searchCategory"));
        page = Integer.parseInt(StringUtil.nvl(multi.getParameter("page"), "1"));
        pageSize = Integer.parseInt(StringUtil.nvl(multi.getParameter("pageSize"), "10"));

        // 페이징 처리
        offset = (page - 1) * pageSize;
        limit = pageSize;
    }

    public String getQueryString() throws UnsupportedEncodingException {
        return String.format(
                "page=%s&fromDate=%s&toDate=%s&search=%s&searchCategory=%s",
                page, fromDate, toDate, URLEncoder.encode(search, "UTF-8") , searchCategory);
    }
}
