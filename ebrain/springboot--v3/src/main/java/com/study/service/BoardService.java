package com.study.service;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.mapper.BoardMapper;
import com.study.mapper.FileMapper;
import com.study.util.FileUtil;
import com.study.validation.BoardValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * 게시글 관련된 비즈니스 로직을 처리하는 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;

    /**
     * 게시글을 등록합니다.
     * 첨부파일이 존재하면 첨부파일 등록
     * @param boardDto
     * @return
     */
    public Long register(BoardDto boardDto) {
        boardDto.setPassword(BoardValidation.encrypt(boardDto.getPassword()));
        boardMapper.insert(boardDto);
        Long boardId = boardDto.getBoardId();

        // 첨부파일이 존재하면 트랜잭션내에서 등록합니다.
        List<FileDto> files = boardDto.getFiles();
        if (files != null) {
            for (FileDto file : files) {
                file.setBoardId(boardId);
                fileMapper.insert(file);
            }
        }

        return boardId;
    }

    /**
     * 게시글을 조회합니다.
     * @param boardId
     * @return BoardDto
     */
    @Transactional(readOnly = true)
    public BoardDto findById(Long boardId) {
        return boardMapper.selectById(boardId);
    }

    /**
     * 게시글을 검색조건에 맞게 조회합니다.
     * @param condition 검색조건
     * @return
     */
    @Transactional(readOnly = true)
    public List<BoardDto> findByCondition(BoardSearchCondition condition) {
        return boardMapper.selectByCondition(condition);
    }

    /**
     * 검색조건에 해당하는 게시글 총 갯수를 조회합니다
     * @param condition 검색조건
     * @return 게시글 총 갯수
     */
    @Transactional(readOnly = true)
    public int countByCondition(BoardSearchCondition condition) {
        return boardMapper.countByCondition(condition);
    }

    /**
     * 게시글을 수정합니다.
     * 첨부파일이 존재하면 첨부파일을 등록합니다.
     * @param boardDto
     * @return 업데이트 행 갯수
     */
    public int update(BoardDto boardDto) {
        // 첨부파일이 존재하면 트랜잭션 내에서 등록합니다.
        boardDto.setPassword(BoardValidation.encrypt(boardDto.getPassword()));
        List<FileDto> files = boardDto.getFiles();

        if (files != null) {
            for (FileDto file : files) {
                file.setBoardId(boardDto.getBoardId());
                fileMapper.insert(file);
            }
        }


        // 삭제할 첨부파일이 존재하면 트랜잭션 내에서 삭제합니다.
        String[] fileIds = boardDto.getFileIds();
        if (fileIds != null) {
            for (String fileIdStr : fileIds) {
                Long fileId = Long.parseLong(fileIdStr);
                FileDto findFile = fileMapper.selectById(fileId);

                // 업로드된 실제 파일을 삭제 합니다.
                File uploadedFile = FileUtil.createFile(findFile.getPhysicalName());
                if (uploadedFile.exists()) {
                    if (uploadedFile.delete()) {
                        // 첨부파일을 삭제 합니다.
                        fileMapper.delete(fileId);
                    }
                }
            }
        }


        return boardMapper.update(boardDto);
    }

    /**
     * 조회수를 1 증가시킵니다.
     * @param boardId
     */
    public void increaseViewCnt(Long boardId) {
        boardMapper.increaseViewCnt(boardId);
    }

    /**
     * 게시글과 관련된 댓글, 첨부파일을 전부 삭제합니다.
     * 첨부된 실제 파일이 존재하면 삭제합니다.
     * @param boardId
     * @return 삭제행 갯수
     */
    public int delete(Long boardId) {
        // 업로드된 실제파일을 삭제합니다.
        List<FileDto> files = fileMapper.selectByBoardId(boardId);
        for (FileDto file : files) {
            File uploadedFile = FileUtil.createFile(file.getPhysicalName());
            if (uploadedFile.exists()) {
                uploadedFile.delete();
            }
        }

        return boardMapper.delete(boardId);
    }
}
