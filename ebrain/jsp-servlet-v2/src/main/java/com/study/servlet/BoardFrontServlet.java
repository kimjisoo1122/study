package com.study.servlet;

import com.study.servlet.board.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/board/*")
public class BoardFrontServlet extends HttpServlet {

    private Map<String, ServletHandler> requestHandlerMap;

    @Override
    public void init() throws ServletException {
        HashMap<String, ServletHandler> map = new HashMap<>();
        map.put("/board", new BoardListHandler());
        map.put("/board/register", new BoardRegisterHandler());
        map.put("/board/board", new BoardHandler());
        map.put("/board/update", new BoardUpdateHandler());
        map.put("/board/replyRegister", new ReplyRegisterHandler());
        requestHandlerMap = map;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO 리퀘스트URI 파싱한 후 맵에서 핸들할 수 있는 핸들러를 가져온다 없는 경우 - 에러발생
        // 핸들러가 doget실행하면 view를 반환한다
        // view를 viewresolver가 분석한후
        // 프론트서블릿이 포워딩
        // 프론트서블릿은 핸들러를 조회하고 실행하고 반환한 뷰를 반환만 할 것임.

        // TODO GET요청과 POST요청 구분
        String requestURI = request.getRequestURI().replace(".jsp", "");
        ServletHandler handler = requestHandlerMap.get(requestURI);
        if (handler == null) {
            handler = new BoardListHandler();
        }
        handler.getHandle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI().replace(".jsp", "");
        ServletHandler handler = requestHandlerMap.get(requestURI);
        if (handler == null) {
            handler = new BoardListHandler();
        }
        handler.postHandle(request, response);
    }
}
