package com.study.repository;

import com.study.dto.CategoryDto;
import com.study.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CategoryMapper categoryMapper;

    public List<CategoryDto> selectAll() {
        return categoryMapper.selectAll();
    }
}
