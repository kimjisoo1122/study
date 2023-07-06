package com.study.service;

import com.study.config.MyBatisSqlSessionFactory;
import com.study.dto.FileDto;
import com.study.mapper.FileMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService {

    private static final FileService FILE_SERVICE = new FileService();


    /**
     * 첨부파일을 등록합니다
     * @param fileDto
     * @return fileId
     */
    public Long save(FileDto fileDto) {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        Long fileId = null;

        try {
            FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
            fileMapper.insert(fileDto);
            fileId = fileDto.getFileId();
            if (fileId == null) {
                throw new IllegalStateException();
            }

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new IllegalStateException("첨부파일 등록에 실패하였습니다.", e);
        } finally {
            sqlSession.close();
        }

        return fileId;
    }

    /**
     * 게시글번호로 첨부파일을 조회합니다.
     * @param boardId
     * @return List<FileDto>
     */
    public List<FileDto> findByBoardId(Long boardId) {
        List<FileDto> files = null;

        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)) {
            FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
            files = fileMapper.findByBoardId(boardId);
        } catch (Exception e) {
            throw new IllegalStateException("첨부파일 조회에 실패하였습니다.", e);
        }

        return files;
    }


    /**
     * 첨부파일을 조회합니다.
     * @param fileId
     * @return
     */

    public FileDto findById(Long fileId) {
        FileDto fileDto = null;

        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)) {
            FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
            fileDto = fileMapper.findById(fileId);
        } catch (Exception e) {
            throw new IllegalStateException("첨부파일 조회에 실패하였습니다.", e);
        }

        return fileDto;
    }

    /**
     * 첨부파일을 삭제합니다.
     * @param fileId
     * @return 삭제행 갯수
     */
    public int delete(Long fileId) {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        int deletedRow = 0;

        try {
            FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
            deletedRow = fileMapper.delete(fileId);

            // TODO 실제파일 삭제

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new IllegalStateException("첨부파일 삭제에 실패하였습니다.", e);
        } finally {
            sqlSession.close();
        }

        return deletedRow;
    }

    public static FileService getFileService() {
        return FILE_SERVICE;
    }
}
