package com.study.dto;

import com.study.util.StringUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

@Data
public class BoardDto {

    enum BoardDtoFields{
        boardId,
    }

    private Long boardId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String writer;
    private String content;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private int viewCnt;
    private boolean hasFile;

    public String getFormattedCreateDate() {
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
    public String getFormattedUpdateDate() {
        return updateDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    public void setRegisterByReq(HttpServletRequest request) {
        writer = request.getParameter("writer");
        password = request.getParameter("password");
        title = request.getParameter("title");
        content = request.getParameter("content");
        String categoryId = request.getParameter("categoryId");
    }
}
