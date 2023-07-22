package com.study.controller;

import com.study.api.ResponseDto;
import com.study.api.ResponseApiStatus;
import com.study.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 카테고리를 처리하는 API컨트롤러 입니다.
 */
@RestController
@RequiredArgsConstructor()
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리를 조회합니다.
     * @return categories 카테고리목록
     */
    @GetMapping("/api/categories")
    public ResponseEntity<ResponseDto> findCategoryList() {
        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseApiStatus.SUCCESS);
        response.setData(Map.of("categories", categoryService.findCategoryList()));

        return ResponseEntity.ok(response);
    }
}