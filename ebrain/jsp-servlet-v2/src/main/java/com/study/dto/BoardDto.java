package com.study.dto;

import lombok.Data;

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
    private String title;
    private String writer;
    private String content;
    private String password;
    private int viewCnt;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // 게시글을 등록할때 해당 리스트에 값이 존재하면 첨부파일을 등록합니다.
    private List<FileDto> files;
    // 게시글을 수정할때 삭제대상 파일목록
    private String[] fileIds;

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
