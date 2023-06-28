package com.study.dao;

import com.study.dto.FileDto;
import com.study.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDao {

    public long save(FileDto fileDto) {
        String sql = "insert into file " +
                "(board_id, name, path, original_name)" +
                "values(?, ?, ?, ?)";

        long fileId = 0;

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, fileDto.getBoardId());
            pstmt.setString(2, fileDto.getName());
            pstmt.setString(3, fileDto.getPath());
            pstmt.setString(4, fileDto.getOriginalName());
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

    public List<FileDto> findByBoardId(Long boardId) {
        String sql =
                "select file_id, name, path, original_name " +
                "from file " +
                "where board_id = ?";

        List<FileDto> files = new ArrayList<>();

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, boardId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FileDto fileDto = new FileDto();
                    fileDto.setFileId(rs.getLong("file_id"));
                    fileDto.setName(rs.getString("name"));
                    fileDto.setPath(rs.getString("path"));
                    fileDto.setOriginalName(rs.getString("original_name"));
                    files.add(fileDto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return files;
    }

    public FileDto findById(Long fileId) {
        String sql =
                "select file_id, board_id, name, path, original_name " +
                "from file " +
                "where file_id = ?";

        FileDto fileDto = new FileDto();

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, fileId);

            try (ResultSet rs =  pstmt.executeQuery()) {
                while (rs.next()) {
                    fileDto.setFileId(rs.getLong("file_id"));
                    fileDto.setBoardId(rs.getLong("board_id"));
                    fileDto.setName(rs.getString("name"));
                    fileDto.setPath(rs.getString("path"));
                    fileDto.setOriginalName(rs.getString("original_name"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fileDto;
    }
}
