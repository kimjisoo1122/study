package com.study.dao;

import com.study.dto.FileDto;
import com.study.util.ConnectionUtil;

import java.sql.*;

public class FileDao {

    public long save(FileDto fileDto) {
        String sql = "insert into file " +
                "(board_id, name, path)" +
                "values(?, ?, ?)";

        long fileId = 0;

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, fileDto.getBoardId());
            pstmt.setString(2, fileDto.getName());
            pstmt.setString(3, fileDto.getPath());
            pstmt.executeUpdate();

            try (
                    ResultSet rs = pstmt.getGeneratedKeys()) {
                while (rs.next()) {
                    fileId = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileId;
    }

    public int delete(long fileId) {
        String sql = "delete from file where file_id = ?";

        int deletedRowCnt = 0;
        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, fileId);
            deletedRowCnt = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedRowCnt;
    }
}
