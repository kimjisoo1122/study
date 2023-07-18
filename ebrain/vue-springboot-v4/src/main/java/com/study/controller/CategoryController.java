package com.study.controller;

import com.study.api.ResponseDto;
import com.study.api.ResponseStatus;
import com.study.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor()
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<ResponseDto> getCategories() {
        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(Map.of("categories", categoryService.findAll()));

        return ResponseEntity.ok(response);
    }
}