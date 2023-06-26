package com.study.dao;

import com.study.dto.BoardDto;
import com.study.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BoardDao {

    public void register(BoardDto boardDto) {
        String sql = "insert into board" +
                "(category_id, title, writer, content, password) " +
                "values(?, ?, ?, ?, ?)";

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, boardDto.getCategoryId());
            pstmt.setString(2, boardDto.getTitle());
            pstmt.setString(3, boardDto.getWriter());
            pstmt.setString(4, boardDto.getContent());
            pstmt.setString(5, boardDto.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BoardDao boardDao = new BoardDao();
        BoardDto boardDto = new BoardDto(1L, "test", "test", "test", "passw");
        boardDao.register(boardDto);
    }
}
