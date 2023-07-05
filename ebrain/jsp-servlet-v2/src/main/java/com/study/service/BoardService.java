package com.study.service;

import com.study.config.MyBatisSqlSessionFactory;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.mapper.BoardMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class BoardService {

    private SqlSessionFactory sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();

    public List<BoardDto> findAllByCondition(BoardSearchCondition condition) {
        List<BoardDto> boards = null;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            BoardMapper mapper = sqlSession.getMapper(BoardMapper.class);
            boards = mapper.findAllByCondition(condition);
        }
        return boards;
    }
}
