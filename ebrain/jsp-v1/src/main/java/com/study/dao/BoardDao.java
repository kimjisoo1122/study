package com.study.dao;

import com.study.dto.BoardDto;
import com.study.util.ConnectionUtil;

import java.sql.*;

public class BoardDao {

    public long register(BoardDto boardDto) {
        String sql = "insert into board" +
                "(category_id, title, writer, content, password) " +
                "values(?, ?, ?, ?, ?)";

        long boardId = 0L;

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, boardDto.getCategoryId());
            pstmt.setString(2, boardDto.getTitle());
            pstmt.setString(3, boardDto.getWriter());
            pstmt.setString(4, boardDto.getContent());
            pstmt.setString(5, boardDto.getPassword());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    boardId = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardId;
    }

    public int delete(long boardId) {
        String sql = "delete from board where board_id = ?";

        int deletedRowCnt = 0;
        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, boardId);
            deletedRowCnt = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedRowCnt;
    }

    public BoardDto findById(Long boardId) {
        String sql =
                "select b.board_id, b.category_id, c.name as category_name," +
                        "b.writer, b.title, b.content, b.password," +
                        "b.view_cnt, b.create_date, b.update_date " +
                "from board b " +
                "join category c on b.category_id = c.category_id " +
                "where board_id = ?";

        BoardDto boardDto = new BoardDto();

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, boardId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    boardDto.setBoardId(rs.getLong("board_id"));
                    boardDto.setCategoryId(rs.getLong("category_id"));
                    boardDto.setCategoryName(rs.getString("category_name"));
                    boardDto.setWriter(rs.getString("writer"));
                    boardDto.setTitle(rs.getString("title"));
                    boardDto.setContent(rs.getString("content"));
                    boardDto.setPassword(rs.getString("password"));
                    boardDto.setViewCnt(rs.getInt("view_cnt"));
                    boardDto.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                    boardDto.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardDto;
    }
}
