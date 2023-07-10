package com.study.dto;

import lombok.Data;

@Data
public class CategoryDto {

    private Long categoryId;
    private String categoryName;
    private Long parentId;
    private String parentName;

    public CategoryDto(Long categoryId, String categoryName, Long parentId, String parentName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.parentName = parentName;
    }
}
