package com.study.service;

import com.study.dto.CategoryDto;
import com.study.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카테고리를 처리하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리를 조회합니다.
     * @return 맵의 키 : 부모 카테고리 이름, 맵의 값 : 자식 카테고리
     */
    public List<CategoryDto> findCategoryList() {
        return categoryRepository.selectAll();
    }
}
