package com.study.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 첨부파일 DTO 입니다.
 */
@Data
public class FileDto {

    private Long fileId; // 파일번호
    private Long boardId; // 게시글번호
    private String physicalName; // 서버에 저장되는 물리적인 이름
    private String originalName; // 실제 업로드된 파일이름
    private String path; // 파일경로
    private String fileExtension; // 파일확장자
    private long fileSize; // 파일크기
    private LocalDateTime createDate; // 생성일시

    /**
     * 파일의 실제 저장된 경로를 반환합니다.
     * @return FullPath
     */
    public String getFullPath() {
        return path + physicalName;
    }
}
