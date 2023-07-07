package com.study.servlet.board;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.CategoryDto;
import com.study.page.PageHandler;
import com.study.service.BoardService;
import com.study.service.CategoryService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 게시글 목록을 조회하는 서블릿 입니다.
 */
public class BoardListServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final CategoryService categoryService = CategoryService.getCategoryService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model)  {
        // 게시글 검색조건을 설정합니다.
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        // 게시글을 조회합니다.
        List<BoardDto> boards = boardService.findByCondition(condition);
        // 게시글의 총 갯수를 조회합니다.
        int totalCnt = boardService.countByCondition(condition);
        // 페이징처리 객체를 생성합니다.
        PageHandler pageHandler = new PageHandler(condition.getPage(), totalCnt);
        // 카테고리를 조회합니다.
        Set<Map.Entry<String, List<CategoryDto>>> categories = categoryService.findAll().entrySet();

        model.put("condition", condition);
        model.put("boards", boards);
        model.put("pageHandler", pageHandler);
        model.put("categories", categories);

        return "board/boardList";
    }
}