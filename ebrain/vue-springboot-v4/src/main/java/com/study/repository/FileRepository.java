package com.study.repository;

import com.study.dto.FileDto;
import com.study.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final FileMapper fileMapper;

    public Long insert(FileDto fileDto) {
        return fileMapper.insert(fileDto);
    }

    public List<FileDto> selectByBoardId(Long boardId) {
        return fileMapper.selectByBoardId(boardId);
    }

    public FileDto selectByFileId(Long fileId) {
        return fileMapper.selectByFileId(fileId);
    }

    public int delete(Long fileId) {
        return fileMapper.delete(fileId);
    }

    public int deleteByBoardId(Long boardId) {
        return fileMapper.deleteByBoardId(boardId);
    }

}
