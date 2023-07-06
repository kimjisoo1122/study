package com.study.config;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MyBatisSqlSessionFactoryTest {

    @Test
    void myBatisConfig() {
        SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true);
        Assertions.assertNotNull(sqlSession);
    }
}