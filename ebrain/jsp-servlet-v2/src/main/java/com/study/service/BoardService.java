package com.study.service;

import com.study.config.MyBatisSqlSessionFactory;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.mapper.BoardMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

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
            // 게시글 등록
            boardMapper.insert(boardDto);
            boardId = boardDto.getBoardId();
            if (boardId == null) {
                throw new IllegalStateException();
            }

            // 첨부파일 등록
            if (boardDto.getFileDto() != null) {
                FileService fileService = FileService.getFileService();
                fileService.save(boardDto.getFileDto());

                // TODO 멀티파트에서 파일저장 하고 들어오나? 체크
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
            boardDto = boardMapper.findById(boardId);
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
    public List<BoardDto> findAllByCondition(BoardSearchCondition condition) {
        List<BoardDto> boards = null;
        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true);){
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
            boards = boardMapper.findAllByCondition(condition);
        } catch (Exception e) {
            throw new IllegalStateException("조회에 실패하였습니다.", e);
        }
        return boards;
    }

    /**
     * 검색조건에 해당하는 게시글 총 갯수 조회
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
     * @param boardDto
     * @return 업데이트 행 갯수
     */
    public int update(BoardDto boardDto) {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        int updatedRow = 0;

        try {
            BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
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
     * 게시글을 삭제하고 삭제된 행 크기 반환
     * @param boardId
     * @return 삭제행 갯수
     */
    public int delete(Long boardId) {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        int deletedRow = 0;

        try {

            //TODO 파일 찾아서 삭제 파일DTO 조회

            // 댓글 삭제 -> 첨부파일 삭제 -> 게시글 삭제 (db delete cascade)
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

    public static BoardService getBoardService() {
        return BOARD_SERVICE;
    }
}
