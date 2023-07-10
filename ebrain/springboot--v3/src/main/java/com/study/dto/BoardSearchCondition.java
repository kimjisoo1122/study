package com.study.dto;

import com.study.util.StringUtil;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 게시글 검색 조건을 설정하는 클래스입니다.
 */
@Data
public class BoardSearchCondition {

    // 게시글 검색조건
    private String searchCategory;
    private String search;
    private String fromDate;
    private String toDate;

    // 페이징처리에 사용합니다.
    private int page; // 현재페이지
    private int pageSize; // 페이지 크기

    // 조회 SQL에 사용합니다.
    private int offset;
    private int limit;
    /**
     * 멀티파트 폼 요청시에 게시글 검색조건을 설정합니다.
     * @param multi
     * @throws UnsupportedEncodingException 검색조건중에 한글을 인코딩합니다.
     */
//    public void setConditionByMulti(MultipartRequest multi) throws UnsupportedEncodingException {
//        fromDate = StringUtil.nvl(multi.getParameter("fromDate")) ;
//        toDate = StringUtil.nvl(multi.getParameter("toDate"));
//        search = URLDecoder.decode(StringUtil.nvl(multi.getParameter("search")), "UTF-8");
//        searchCategory = StringUtil.nvl(multi.getParameter("searchCategory"));
//        page = Integer.parseInt(StringUtil.nvl(multi.getParameter("page"), "1"));
//        pageSize = Integer.parseInt(StringUtil.nvl(multi.getParameter("pageSize"), "10"));
//
//        // 페이징 처리
//        offset = (page - 1) * pageSize;
//        limit = pageSize;
//    }

    /**
     * 서블릿에서 사용하는 리퀘스트 정보를
     * 담은 맵에서 검색조건들을 설정 합니다.
     * @param paramMap
     */
    public void setConditionByParam(Map<String, String> paramMap) {
        fromDate = StringUtil.nvl(paramMap.get("fromDate"));
        toDate = StringUtil.nvl(paramMap.get("toDate"));
        search = StringUtil.nvl(paramMap.get("search"));
        try {
            search = URLDecoder.decode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        searchCategory = StringUtil.nvl(paramMap.get("searchCategory"));
        page = Integer.parseInt(StringUtil.nvl(paramMap.get("page"), "1"));
        pageSize = Integer.parseInt(StringUtil.nvl(paramMap.get("pageSize"), "10"));

        // 페이징 처리
        offset = (page - 1) * pageSize;
        limit = pageSize;
    }

    /**
     * 검색조건들을 쿼리스트링으로 변환합니다.
     * @return 리다이렉트에 사용하는 쿼리스트링
     * @throws UnsupportedEncodingException 검색조건중에 한글을 인코딩합니다.
     */
    public String getQueryString() throws UnsupportedEncodingException {
        return String.format(
                "?page=%s&fromDate=%s&toDate=%s&search=%s&searchCategory=%s",
                page, fromDate, toDate, URLEncoder.encode(search, "UTF-8") , searchCategory);
    }

    public String getQueryParamString(int page) throws UnsupportedEncodingException {
        return String.format(
                "?page=%s&fromDate=%s&toDate=%s&search=%s&searchCategory=%s",
                page, fromDate, toDate, search , searchCategory);
    }
}