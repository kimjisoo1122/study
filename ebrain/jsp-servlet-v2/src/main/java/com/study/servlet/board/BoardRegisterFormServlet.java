package com.study.servlet.board;

import com.study.dto.BoardSearchCondition;
import com.study.dto.CategoryDto;
import com.study.service.CategoryService;

import java.util.List;
import java.util.Map;

/**
 * 게시글을 등록하는 폼을 조회하는 서블릿 입니다.
 */
public class BoardRegisterFormServlet implements MyServlet {

    private final CategoryService categoryService = CategoryService.getCategoryService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) {
        // 검색조건을 설정합니다.
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        // 카테고리를 조회합니다.
        Map<String, List<CategoryDto>> categories = categoryService.findAll();

        model.put("condition", condition);
        model.put("categories", categories);

        return "board/register";
    }
}
