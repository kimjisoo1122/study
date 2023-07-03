package com.study.dto;

public class CategoryDto {

    private Long categoryId;
    private String name;
    private Long parentId;
    private String parentName;

    public CategoryDto(Long categoryId, String name, Long parentId, String parentName) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
