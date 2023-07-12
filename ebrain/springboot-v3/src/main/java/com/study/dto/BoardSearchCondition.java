package com.study.dto;

import com.study.util.StringUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

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
    private String fromDate; //
    private String toDate;
    private String search;
    private String searchCategory ;

    // 페이징처리에 사용합니다.
    private int page; // 현재 페이지
    private int pageSize; // 페이지 크기

    // 조회 SQL에 사용합니다.
    private int offset;
    private int limit;

    public BoardSearchCondition() {
        setParamNvl();
    }

    /**
     * 검색조건들을 쿼리스트링으로 변환합니다.
     * @return 리다이렉트에 사용하는 쿼리스트링
     */
    public String getQueryParamString(int page) {
        return String.format(
                "?page=%s&fromDate=%s&toDate=%s&search=%s&searchCategory=%s",
                page, fromDate, toDate, search , searchCategory);
    }

    public String getQueryParamString() {
        return getQueryParamString(page);
    }

    /**
     * queryParameter로 넘어오는 파라미터의 null값을 체크합니다.
     */
    private void setParamNvl() {
        fromDate = StringUtil.nvl(fromDate);
        toDate = StringUtil.nvl(toDate);
        search = StringUtil.nvl(search);
        searchCategory = StringUtil.nvl(searchCategory);
    }
}