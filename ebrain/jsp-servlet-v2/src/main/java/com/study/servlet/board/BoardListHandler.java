package com.study.servlet.board;

import com.study.dao.CategoryDao;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.CategoryDto;
import com.study.page.PageHandler;
import com.study.service.BoardService;
import com.study.servlet.ServletHandler;
import com.study.util.JspViewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BoardListHandler implements ServletHandler {

    private final BoardService boardService = BoardService.getBoardService();

    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 웹페이지 뒤로가기 캐시 방지 (조회수 반영)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        // 검색조건 설정
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);

        // 게시글 조회
        List<BoardDto> boards = boardService.findAllByCondition(condition);

        // 총 게시글 조회
        int totalCnt = boardService.countByCondition(condition);

        CategoryDao categoryDao = new CategoryDao();
        Set<Map.Entry<String, List<CategoryDto>>> categories = categoryDao.findAll().entrySet();

        // 페이징 처리
        PageHandler pageHandler = new PageHandler(condition.getPage(), totalCnt);

        // 모델 설정
        request.setAttribute("condition", condition);
        request.setAttribute("boards", boards);
        request.setAttribute("pageHandler", pageHandler);
        request.setAttribute("categories", categories);

        // 포워딩
        String path = JspViewResolver.getViewPath("/board/boardList");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getHandle(request, response);
    }
}
