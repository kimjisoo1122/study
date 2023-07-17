package com.study.dto;

import lombok.Data;

/**
 * 카레고리 DTO 입니다.
 */
@Data
public class CategoryDto {

    private Long categoryId; // 카테고리 번호
    private String categoryName; // 카테고리 이름
    private Long parentId; // 상위카테고리 번호
    private String parentName; // 상위카테고리 이름
}
