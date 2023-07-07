package com.study.service;

import com.study.config.MyBatisSqlSessionFactory;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.mapper.BoardMapper;
import com.study.mapper.FileMapper;
import com.study.util.FileUtil;
import com.study.validation.BoardValidation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.util.List;

/**
 * 게시글 관련된 비즈니스 로직을 처리하는 서비스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardService {

    private static final BoardService BOARD_SERVICE = new BoardService();

    /**
     * 게시글을 등록합니다.
     * 첨부파일이 존재하면 첨부파일 등록
     * @param boardDto
     * @return
     */
    public Long register(BoardDto boardDto) {
        Long boardId = null;
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        try {
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);

            // 비밀번호를 암호화 합니다.
            boardDto.setPassword(BoardValidation.encrypt(boardDto.getPassword()));

            // 게시글 등록
            boardMapper.insert(boardDto);
            boardId = boardDto.getBoardId();
            if (boardId == null) {
                throw new IllegalStateException();
            }

            // 첨부파일이 존재하면 트랜잭션내에서 등록합니다.
            List<FileDto> files = boardDto.getFiles();
            if (files != null && !files.isEmpty()) {
                FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
                for (FileDto file : files) {
                    file.setBoardId(boardId);
                    fileMapper.insert(file);
                }
            }

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new IllegalStateException("게시글 등록에 실패 하였습니다.", e);
        } finally {
            sqlSession.close();
        }
        return boardId;
    }

    /**
     * 게시글을 조회합니다.
     * @param boardId
     * @return BoardDto
     */
    public BoardDto findById(Long boardId) {
        BoardDto boardDto = null;
        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)){
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
            boardDto = boardMapper.selectById(boardId);
        } catch (Exception e) {
            throw new IllegalStateException("게시글 조회에 실패 하였습니다.", e);
        }

        return boardDto;
    }

    /**
     * 게시글을 검색조건에 맞게 조회합니다.
     * @param condition 검색조건
     * @return
     */
    public List<BoardDto> findByCondition(BoardSearchCondition condition) {
        List<BoardDto> boards = null;
        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true);){
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
            boards = boardMapper.selectByCondition(condition);
        } catch (Exception e) {
            throw new IllegalStateException("조회에 실패하였습니다.", e);
        }
        return boards;
    }

    /**
     * 검색조건에 해당하는 게시글 총 갯수를 조회합니다
     * @param condition 검색조건
     * @return 게시글 총 갯수
     */
    public int countByCondition(BoardSearchCondition condition) {
        int count = 0;

        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)){
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
            count = boardMapper.countByCondition(condition);
        } catch (Exception e) {
            throw new IllegalStateException("게시글 조회에 실패 하였습니다.", e);
        }

        return count;
    }

    /**
     * 게시글을 수정합니다.
     * 첨부파일이 존재하면 첨부파일을 등록합니다.
     * @param boardDto
     * @return 업데이트 행 갯수
     */
    public int update(BoardDto boardDto) {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        int updatedRow = 0;

        try {
            // 비밀번호를 암호화 합니다.
            boardDto.setPassword(BoardValidation.encrypt(boardDto.getPassword()));

            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);

            // 첨부파일이 존재하면 트랜잭션 내에서 등록합니다.
            List<FileDto> files = boardDto.getFiles();
            if (files != null && !files.isEmpty()) {
                FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
                for (FileDto file : files) {
                    file.setBoardId(boardDto.getBoardId());
                    fileMapper.insert(file);
                }
            }

            // 삭제할 첨부파일이 존재하면 트랜잭션 내에서 삭제합니다.
            String[] fileIds = boardDto.getFileIds();
            if (fileIds != null) {
                for (String fileIdStr : fileIds) {
                    FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
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

            updatedRow = boardMapper.update(boardDto);

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new IllegalStateException("게시글 수정에 실패하였습니다.", e);
        } finally {
            sqlSession.close();
        }

        return updatedRow;
    }

    public void increaseViewCnt() {
        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)) {
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
            boardMapper.increaseViewCnt();
        } catch (Exception e) {
            throw new IllegalStateException("게시글 조회수 증가에 실패하였습니다.", e);
        }
    }

    /**
     * 게시글과 관련된 댓글, 첨부파일을 전부 삭제합니다.
     * 첨부된 실제 파일이 존재하면 삭제합니다.
     * @param boardId
     * @return 삭제행 갯수
     */
    public int delete(Long boardId) {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        int deletedRow = 0;

        try {
            // 업로드된 실제파일을 삭제합니다.
            FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
            List<FileDto> files = fileMapper.selectByBoardId(boardId);
            for (FileDto file : files) {
                File uploadedFile = FileUtil.createFile(file.getPhysicalName());
                if (uploadedFile.exists()) {
                    uploadedFile.delete();
                }
            }

            // 댓글 삭제 -> 첨부파일 삭제 -> 게시글 삭제 (외래키 ON DELETE CASCADE)
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
            deletedRow = boardMapper.delete(boardId);

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new IllegalStateException("게시글 삭제에 실패 하였습니다.", e);
        } finally {
            sqlSession.close();
        }

        return deletedRow;
    }

    /**
     * @return BOARD_SERVICE 싱글톤 객체 반환
     */
    public static BoardService getBoardService() {
        return BOARD_SERVICE;
    }
}
