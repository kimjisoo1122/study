package com.study.service;

import com.study.dto.BoardDto;
import com.study.dto.FileDto;
import com.study.util.FileUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    static FileService fileService = FileService.getFileService();
    static BoardService boardService = BoardService.getBoardService();

    @Test
    void save() {
        // 게시글 등록
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setWriter("테스트");
        boardDto.setTitle("테스트제목");
        boardDto.setContent("테스트내용");
        boardDto.setPassword("test1234!");
        Long boardId = boardService.register(boardDto);
        assertNotNull(boardId);

        // 첨부파일 저장
        FileDto fileDto = new FileDto();
        fileDto.setBoardId(boardId);
        fileDto.setPhysicalName("다운로드명.jpg");
        fileDto.setOriginalName("오리지널.jpg");
        fileDto.setPath(FileUtil.FILE_PATH);
        fileDto.setFileSize(1023012);
        fileDto.setFileExtension("jpg");
        Long fileId = fileService.save(fileDto);

        FileDto saveFile = fileService.findById(fileId);
        assertNotNull(saveFile);
        assertEquals(fileDto.getOriginalName(), saveFile.getOriginalName());
        assertEquals(fileDto.getPhysicalName(), saveFile.getPhysicalName());

        // 게시글 롤백 -> 첨부파일 함께 삭제
        int deletedRow = boardService.delete(boardId);
        assertEquals(1, deletedRow);

        FileDto deletedFile = fileService.findById(fileId);
        assertNull(deletedFile);
    }

    @Test
    void findByBoardId() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void getFileService() {
    }
}