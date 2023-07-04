package com.study.dao;

import com.study.dto.ReplyDto;
import com.study.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplyDao {

    public Long register(ReplyDto replyDto) {
        String sql = "insert into reply " +
                "(board_id, content) " +
                "values (?, ?)";

        Long replyId = 0L;

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, replyDto.getBoardId());
            pstmt.setString(2, replyDto.getContent());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    replyId = rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replyId;
    }

    public int delete(Long replyId) {
        String sql = "delete from reply where reply_id = ?";
        int deletedRowCnt = 0;

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, replyId);
            deletedRowCnt = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deletedRowCnt;
    }

    public int deleteByBoardId(Long boardId) {
        String sql = "delete from reply where board_id = ?";
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

    public ReplyDto findById(Long replyId) {
        String sql =
                "select reply_id, board_id, content, " +
                        "create_date, update_date " +
                "from reply " +
                "where reply_id = ?";

        ReplyDto replyDto = new ReplyDto();

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, replyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    replyDto.setReplyId(rs.getLong("reply_id"));
                    replyDto.setBoardId(rs.getLong("board_id"));
                    replyDto.setContent(rs.getString("content"));
                    replyDto.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                    replyDto.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replyDto;
    }

    public List<ReplyDto> findByBoardId(Long boardId) {
        String sql =
                "select reply_id, board_id, content, " +
                        "create_date, update_date " +
                        "from reply " +
                        "where board_id = ? " +
                        "order by create_date";

        List<ReplyDto> replies = new ArrayList<>();

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, boardId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ReplyDto replyDto = new ReplyDto();
                    replyDto.setReplyId(rs.getLong("reply_id"));
                    replyDto.setBoardId(rs.getLong("board_id"));
                    replyDto.setContent(rs.getString("content"));
                    replyDto.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                    replyDto.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());

                    replies.add(replyDto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replies;
    }
}
