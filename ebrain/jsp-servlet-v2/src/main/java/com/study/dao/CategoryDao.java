package com.study.dao;

import com.study.dto.CategoryDto;
import com.study.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryDao {

    public  Map<String, List<CategoryDto>> findAll() {
        String sql =
                "select c1.category_id, c1.name, c1.parent_id, c2.name parent_name " +
                "from category c1 " +
                "join category c2 on c1.parent_id = c2.category_id";
        List<CategoryDto> categories = new ArrayList<>();

        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categories.add(
                        new CategoryDto(rs.getLong("category_id"),
                                rs.getString("name"),
                                rs.getLong("parent_id"),
                                rs.getString("parent_name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories.stream()
                .collect(Collectors.groupingBy(CategoryDto::getParentName));
    }
}
