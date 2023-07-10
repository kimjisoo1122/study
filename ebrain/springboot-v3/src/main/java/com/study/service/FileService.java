package com.study.service;

import com.study.dto.FileDto;
import com.study.mapper.FileMapper;
import com.study.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * 첨부파일의 비즈니스로직을 처리하는 서비스 입니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;
    /**
     * 첨부파일을 등록합니다
     * @param fileDto
     * @return fileId
     */
    public Long save(FileDto fileDto) {
        fileMapper.insert(fileDto);
        return fileDto.getFileId();
    }

    /**
     * 게시글번호로 첨부파일을 조회합니다.
     * @param boardId
     * @return List<FileDto>
     */
    @Transactional(readOnly = true)
    public List<FileDto> findByBoardId(Long boardId) {
        return fileMapper.selectByBoardId(boardId);
    }


    /**
     * 첨부파일을 조회합니다.
     * @param fileId
     * @return
     */
    @Transactional(readOnly = true)
    public FileDto findById(Long fileId) {
        return fileMapper.selectById(fileId);
    }

    /**
     * 첨부파일과 업로드된 실제파일도 삭제합니다
     * @param fileId
     * @return 삭제행 갯수
     */
    public int delete(Long fileId) {
        int deletedRow = 0;
        FileDto fileDto = fileMapper.selectById(fileId);
        File file = FileUtil.createFile(fileDto.getPhysicalName());
        if (file.exists()) {
            if (file.delete()) {
                deletedRow = fileMapper.delete(fileId);
            }
        }

        return deletedRow;
    }
}
