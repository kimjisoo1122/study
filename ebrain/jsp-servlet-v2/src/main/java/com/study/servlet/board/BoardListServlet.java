package com.study.servlet.board;

import com.study.dao.CategoryDao;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.CategoryDto;
import com.study.page.PageHandler;
import com.study.service.BoardService;
import com.study.servlet.MyServlet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BoardListServlet implements MyServlet {
    
    private final BoardService boardService = BoardService.getBoardService();
    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model)  {
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        // 게시글 조회
        List<BoardDto> boards = boardService.findAllByCondition(condition);
        // 총 게시글 조회
        int totalCnt = boardService.countByCondition(condition);

        // TODO 카테고리 JDBC -> MyBatis 전환
        CategoryDao categoryDao = new CategoryDao();
        Set<Map.Entry<String, List<CategoryDto>>> categories = categoryDao.findAll().entrySet();

        // 페이징 처리
        PageHandler pageHandler = new PageHandler(condition.getPage(), totalCnt);

        model.put("condition", condition);
        model.put("boards", boards);
        model.put("pageHandler", pageHandler);
        model.put("categories", categories);

        return "board/boardList";
    }
}
