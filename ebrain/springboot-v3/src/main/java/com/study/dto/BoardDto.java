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

    @NotNull(message = "카테고리는 필수 항목입니다.")
    private Long categoryId;

    @NotBlank(message = "작성자는 필수 항목입니다.")
    @Size(min = 3, max = 4, message = "작성자는 3글자 이상 5글자 미만 이여야 합니다.")
    private String writer;

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(min = 4, max = 99, message = "제목은 4글자 이상 100글자 미만 이여야 합니다.")
    private String title;

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Size(min = 4, max = 1999, message = "내용은 4글자 이상 2000글자 미만 이여야 합니다.")
    private String content;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "영문/숫자/특수문자를 포함 하여야 합니다.")
    private String password;

    private Long boardId;
    private String categoryName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private int viewCnt;

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
