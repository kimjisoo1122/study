package com.study.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 게시글 DTO 입니다.
 */
@Data
public class BoardDto {

    private Long boardId;
    private Long categoryId;
    private String categoryName;
    private String writer;
    private String title;
    private String password;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private int viewCnt;

    // 첨부파일의 존재유무
    private boolean hasFile;


    /**
     * 게시글에 맞는 포맷된 형식의 생성일시 반환합니다.
     * @return yyyy.MM.dd HH:mm 포맷의 생성일시
     */
    public String getFormattedCreateDate() {
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    /**
     * 게시글에 맞는 포맷된 형식의 수정일시를 반환합니다.
     * @return yyyy.MM.dd HH:mm 포맷의 수정일시
     */
    public String getFormattedUpdateDate() {
        return updateDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
