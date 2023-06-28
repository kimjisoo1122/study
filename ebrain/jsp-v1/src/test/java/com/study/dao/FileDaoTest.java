package com.study.dao;

import com.study.dto.BoardDto;
import com.study.dto.FileDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileDaoTest {

    @Test
    @DisplayName("게시글 첨부 파일 저장")
    void save() throws Exception {
        // 게시글 작성
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setTitle("제목");
        boardDto.setWriter("작성자");
        boardDto.setContent("내용");
        boardDto.setPassword("비밀번호");
        BoardDao boardDao = new BoardDao();
        long boardId = boardDao.register(boardDto);

        // 파일 생성
        FileDto fileDto = new FileDto();
        fileDto.setBoardId(1L);
        fileDto.setName("파일이름");
        fileDto.setPath("경로");
        FileDao fileDao = new FileDao();
        long fileId = fileDao.save(fileDto);

        // 게시글, 파일 롤백
        int deletedFileCnt = fileDao.delete(fileId);
        int deletedBoardCnt = boardDao.delete(boardId);

        // 검증
        assertEquals(1, deletedFileCnt);
        assertEquals(1, deletedBoardCnt);
    }

    @Test
    @DisplayName("게시글 첨부 파일 조회")
    public void findByBoardId() {
        // 게시글 작성
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(1L);
        boardDto.setTitle("제목");
        boardDto.setWriter("작성자");
        boardDto.setContent("내용");
        boardDto.setPassword("비밀번호");
        BoardDao boardDao = new BoardDao();
        long boardId = boardDao.register(boardDto);

        // 파일 생성
        FileDto fileDto = new FileDto();
        fileDto.setBoardId(boardId);
        fileDto.setName("파일이름");
        fileDto.setPath("경로");
        fileDto.setOriginalName("오리지널이름");
        FileDao fileDao = new FileDao();
        long fileId = fileDao.save(fileDto);

        // 첨부파일 조회
        List<FileDto> files = fileDao.findByBoardId(boardId);
        FileDto file = files.get(0);

        assertEquals(1, files.size());
        assertEquals(fileDto.getName(), file.getName());
        assertEquals(fileDto.getPath(), file.getPath());
        assertEquals(fileDto.getOriginalName(), file.getOriginalName());


        // 게시글, 파일 롤백
        int deletedFileCnt = fileDao.delete(fileId);
        int deletedBoardCnt = boardDao.delete(boardId);

        // 검증
        assertEquals(1, deletedFileCnt);
        assertEquals(1, deletedBoardCnt);
    }

}