package com.study.servlet.board;

import com.study.dao.BoardDao;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.page.PageHandler;
import com.study.util.JspViewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BoardListHandler implements Handler{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 웹페이지 뒤로가기 캐시 방지 (조회수 반영)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        // 검색조건 설정
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);
        request.setAttribute("condition", condition);

        // 게시글 조회
        BoardDao boardDao = new BoardDao();
        List<BoardDto> boards = boardDao.findAll(condition);
        request.setAttribute("boards", boards);

        // 총 게시글 조회
        int count = boardDao.count(condition);

        // 페이징 처리
        PageHandler pageHandler = new PageHandler(condition.getPage(), count);
        request.setAttribute("pageHandler", pageHandler);

        // 포워딩
        String path = JspViewResolver.getViewName("/board/boardList");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }
}
