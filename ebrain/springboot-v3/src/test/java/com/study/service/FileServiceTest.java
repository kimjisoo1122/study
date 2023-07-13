package com.study.service;

import com.study.dto.BoardRegisterForm;
import com.study.dto.FileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
@Transactional
class FileServiceTest {

    @Autowired
    FileService fileService;
    @Autowired
    BoardService boardService;

    @Test
    void save() throws IOException {
        // 게시글 등록
        BoardRegisterForm registerForm = new BoardRegisterForm();
        registerForm.setCategoryId(1L);
        registerForm.setWriter("테스트");
        registerForm.setTitle("테스트제목");
        registerForm.setContent("테스트내용");
        registerForm.setPassword("test1234!");
        Long boardId = boardService.register(registerForm);
        assertNotNull(boardId);

        // 첨부파일 저장
        FileDto saveFile = new FileDto();
        saveFile.setBoardId(boardId);
        saveFile.setPhysicalName("다운로드명.jpg");
        saveFile.setOriginalName("오리지널.jpg");
        saveFile.setPath(fileService.getFilePath());
        saveFile.setFileSize(1023012);
        saveFile.setFileExtension("jpg");

        Long saveFileId = fileService.save(saveFile);
        FileDto findFile = fileService.findById(saveFileId);

        assertNotNull(findFile);
        assertEquals(saveFile.getOriginalName(), findFile.getOriginalName());
        assertEquals(saveFile.getPhysicalName(), findFile.getPhysicalName());
    }
}