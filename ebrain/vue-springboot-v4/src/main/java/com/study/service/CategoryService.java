package com.study.service;

import com.study.dto.CategoryDto;
import com.study.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 카테고리를 처리하는 서비스입니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;


    /**
     * 카테고리를 조회합니다.
     * @return 맵의 키 : 부모 카테고리 이름, 맵의 값 : 자식 카테고리
     */
    @Transactional(readOnly = true)
    public Map<String, List<CategoryDto>> findAll() {
        return categoryMapper.selectAll().stream().
                collect(Collectors.groupingBy(CategoryDto::getParentName));
    }
}
