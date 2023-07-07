package com.study.service;

import com.study.config.MyBatisSqlSessionFactory;
import com.study.dto.ReplyDto;
import com.study.mapper.ReplyMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 댓글의 비즈니스로직을 처리하는 서비스 입니다.
 */
public class ReplyService {

    private static final ReplyService REPLY_SERVICE = new ReplyService();

    /**
     * 게시글의 댓글을 등록한 후
     * 등록된 댓글을 조회하여 반환합니다.
     * @param replyDto
     * @return ReplyDto 등록된 댓글객체
     */
    public ReplyDto register(ReplyDto replyDto) {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(false);
        ReplyDto findReply = null;

        try {
            ReplyMapper replyMapper = sqlSession.getMapper(ReplyMapper.class);
            replyMapper.insert(replyDto);
            Long replyId = replyDto.getReplyId();
            if (replyId == null) {
                throw new IllegalStateException();
            }
            // 댓글을 조회합니다.
            findReply = replyMapper.selectById(replyId);

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new IllegalStateException("댓글 등록에 실패하였습니다.", e);
        } finally {
            sqlSession.close();
        }

        return findReply;
    }

    /**
     * 댓글을 조회합니다.
     * @param replyId
     * @return ReplyDto
     */
    public ReplyDto findById(Long replyId) {
        ReplyDto replyDto = null;

        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)) {
            ReplyMapper replyMapper = sqlSession.getMapper(ReplyMapper.class);
            replyDto = replyMapper.selectById(replyId);
        } catch (Exception e) {
            throw new IllegalStateException("댓글 등록에 실패하였습니다.", e);
        }

        return replyDto;
    }

    /**
     * 게시글번호로 댓글을 조회합니다.
     * @param boardId
     * @return List<ReplyDto>
     */
    public List<ReplyDto> findByBoardId(Long boardId) {
        List<ReplyDto> replies = null;

        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)) {
            ReplyMapper replyMapper = sqlSession.getMapper(ReplyMapper.class);
            replies = replyMapper.selectByBoardId(boardId);
        } catch (Exception e) {
            throw new IllegalStateException("댓글 등록에 실패하였습니다.", e);
        }

        return replies;
    }

    /**
     * @return REPLY_SERVICE 싱글톤 객체 반환
     */
    public static ReplyService getReplyService() {
        return REPLY_SERVICE;
    }
}
