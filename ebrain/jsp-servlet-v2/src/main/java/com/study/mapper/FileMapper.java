package com.study.mapper;

import com.study.dto.FileDto;

import java.util.List;

public interface FileMapper {

    Long insert(FileDto fileDto);
    List<FileDto> selectByBoardId(Long boardId);
    FileDto selectById(Long fileId);
    int delete(Long fileId);
    int deleteByBoardId(Long boardId);
}
