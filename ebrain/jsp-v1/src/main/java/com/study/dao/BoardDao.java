package com.study.dao;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.util.ConnectionUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BoardDao {


    /**
     * 게시글 등록
     *
     * @param boardDto
     * @return boardId
     */
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

    /**
     * 게시글 삭제
     *
     * @param boardId
     * @return deletedRowCnt
     */
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

    /**
     * 게시글 조회
     *
     * @param boardId
     * @return boardDto
     */
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

    /**
     * 게시글 동적으로 업데이트
     *
     * @param paramMap
     * @param boardId
     * @return updatedRowCnt
     */
    public int update(Map<String, String> paramMap, Long boardId) {
        if (paramMap.isEmpty()) {
            return 0;
        }

        // 동적쿼리 생성
        StringBuilder sb = new StringBuilder("update board set ");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

        // 맵으로 순회하며 컬럼이 존재하면 추가
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String updateSql = entry.getKey() + " = ? ";
            sb.append(updateSql).append(", ");
            // pstmt로 값 넣어주기 위한 세팅
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        // 업데이트 날짜 추가하며 최종쿼리 완성
        String sql = sb + "update_date = now() where board_id = ?";

        int updatedCnt = 0;

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int idx = 1;
            for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
                String column = entry.getKey();
                String value = entry.getValue();

                switch (column) {
                    case "view_cnt":
                        pstmt.setInt(idx++, Integer.parseInt(value));
                        break;
                    case "category_id":
                        pstmt.setLong(idx++, Long.parseLong(value));
                        break;
                    default:
                        pstmt.setString(idx++, value);
                }
            }

            pstmt.setLong(idx, boardId);
            updatedCnt = pstmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
        return updatedCnt;
    }

    /**
     * 게시글 조회수 증가
     * @param boardId
     */
    public void addViewCnt(Long boardId) {
        String sql =
                "update board " +
                "set view_cnt = view_cnt + 1 " +
                "where board_id = ?";

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, boardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 게시글을 조건(카테고리,검색어,페이징)에 따라 조회한다
     * @param condition
     * @return
     */
    public List<BoardDto> findAll(BoardSearchCondition condition) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select b.board_id, b.category_id, c.name as category_name," +
                        " b.title, b.writer, b.content, b.view_cnt, b.create_date, b.update_date " +
                "from board b join category c " +
                "on b.category_id = c.category_id ");

        List<BoardDto> boards = new ArrayList<>();
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        List<String> pstmtList = new ArrayList<>();

        // 기간검색 조건 없을시 기본 1년간 체크
        if ((condition.getFromDate() == null || condition.getFromDate().isEmpty())
                && (condition.getToDate() == null || condition.getToDate().isEmpty())) {
            LocalDate now = LocalDate.now();
            LocalDate oneYearAgo = now.minusYears(1);
            condition.setFromDate(oneYearAgo.format(DateTimeFormatter.ISO_LOCAL_DATE));
            condition.setToDate(now.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        sb.append("where ");
        sb.append("date_format(b.create_date, '%Y-%m-%d') between ? and ? and");
        linkedHashMap.put("fromDate", condition.getFromDate());
        linkedHashMap.put("toDate", condition.getToDate());

        // 카테고리 조건 (기본 : all)
        if (condition.getCategoryId() != null) {
            sb.append(" b.category_id = ? and");
            linkedHashMap.put("categoryId", String.valueOf(condition.getCategoryId()));
        }

        // 검색어 조건
        if (condition.getSearch() != null || !condition.getSearch().isEmpty()) {
            sb.append(" (b.writer like ? or b.title like ? or b.content like ?) and");
            String search = "%" + condition.getSearch() + "%";
            linkedHashMap.put("search", search);
        }

        // 마지막 and를 제거한다
        String sql = sb.substring(0, sb.length() - 3) + " order by b.create_date desc";
//        // 페이징 처리로 sql 완료
//        int offset = condition.getOffset() == 0 ? 10 : condition.getOffset();
//        sql += " limit " + condition.getLimit() + " offset " + condition.getOffset();

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int idx = 1;
            for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if ("search".equals(key)) {
                    for (int i = 0; i < 3; i++) {
                        pstmt.setString(idx++, value);
                    }
                } else if ("categoryId".equals(key)) {
                    pstmt.setLong(idx++, Long.parseLong(value));
                } else {
                    pstmt.setString(idx++, value);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BoardDto boardDto = new BoardDto();
                    boardDto.setBoardId(rs.getLong("board_id"));
                    boardDto.setCategoryId(rs.getLong("category_id"));
                    boardDto.setCategoryName(rs.getString("category_name"));
                    boardDto.setWriter(rs.getString("writer"));
                    boardDto.setTitle(rs.getString("title"));
                    boardDto.setContent(rs.getString("content"));
                    boardDto.setViewCnt(rs.getInt("view_cnt"));
                    boardDto.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                    boardDto.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());

                    boards.add(boardDto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return boards;
    }

    /**
     * 게시글의 총 갯수
     * @return
     */
    public int totalCnt() {
        int totalCnt = 0;
        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("select count(*) from board")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalCnt = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
    }
}
