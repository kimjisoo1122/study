package com.study.mapper;

import com.study.dto.CategoryDto;

import java.util.List;

public interface CategoryMapper {
    List<CategoryDto> selectAll();
}
