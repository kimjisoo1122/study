package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public class UserDaoJdbcTemplate implements UserDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int deleteUser(String id) {
        return jdbcTemplate.update("delete from user_info where id = ?", new String[]{id});
    }

    @Override
    public User selectUser(String id) throws Exception {
        return jdbcTemplate.queryForObject("select * from user_info where id = ?",new String[]{id} , new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public int insertUser(User user) {
        String sql = "insert into user_info values (?, ?, ?, ?, ?, ?, now()) ";
        return jdbcTemplate.update(sql, pstmt -> {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPwd());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.setDate(5, new Date(user.getBirth().getTime()));
            pstmt.setString(6, user.getSns());
        });
    }

    @Override
    public int updateUser(User user) {
        return 0;
    }
}
