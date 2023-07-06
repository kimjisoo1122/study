package com.study.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDto {

    private Long fileId;
    private Long boardId;
    private String physicalName;
    private String originalName;
    private String path;
    private String fileExtension;
    private long fileSize;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
