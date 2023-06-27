package com.study.dao;

import com.study.dto.BoardDto;
import com.study.dto.FileDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileDaoTest {

    @Test
    void save() throws Exception {
        // 게시글 작성
        BoardDto boardDto =
                new BoardDto(1L, "제목",
                        "작성자", "내용", "비밀번호");
        BoardDao boardDao = new BoardDao();
        long boardId = boardDao.register(boardDto);

        // 파일 생성
        FileDto fileDto = new FileDto(1L, "test.png", "C:\\upload");
        FileDao fileDao = new FileDao();
        long fileId = fileDao.save(fileDto);

        // 게시글, 파일 롤백
        int deletedFileCnt = fileDao.delete(fileId);
        int deletedBoardCnt = boardDao.delete(boardId);

        // 검증
        assertEquals(1, deletedFileCnt);
        assertEquals(1, deletedBoardCnt);
    }

}