package com.study.dto;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class BoardDto {

    private Long boardId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String writer;
    private String content;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private FileDto fileDto;

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
