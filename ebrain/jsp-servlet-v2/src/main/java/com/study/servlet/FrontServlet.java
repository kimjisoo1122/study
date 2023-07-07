package com.study.servlet;

import com.study.exception.HandlerAdapterNotFoundException;
import com.study.servlet.adapter.MyAdapter;
import com.study.servlet.adapter.ServletAdapter;
import com.study.servlet.board.*;
import com.study.servlet.modelview.MyModelAndView;
import com.study.servlet.modelview.MyView;
import com.study.util.JspViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게시판과 관련된 모든 요청을 처리하는 프론트 서블릿입니다.
 * 특정 요청을 처리하는 핸들러를 찾고 어댑터를 통해 실행합니다.
 */
@WebServlet("/board/*")
public class FrontServlet extends HttpServlet {

    // 리다이렉트 경로의 접두사
    private final String redirectPath = "redirect:";

    // GET 요청에 대한 핸들러 맵
    private final Map<String, Object> requestGetMap = new HashMap<>();
    // POST 요청에 대한 핸들러 맵
    private final Map<String, Object> requestPostMap = new HashMap<>();
    // 핸들러 어댑터 목록
    private final List<MyAdapter> adapters = new ArrayList<>();

    /**
     * 서블릿 생성시 요청 맵과 어댑터를 초기화 합니다.
     */
    public FrontServlet() {
        initRequestGetMap();
        initRequestPostMap();
        initAdapters();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 요청된 URI
        String requestURI = request.getRequestURI();
        Object handler = null;
        // GET 또는 POST 요청에 따라 핸들러를 조회합니다.
        if (request.getMethod().equalsIgnoreCase("GET")) {
            handler = requestGetMap.get(requestURI);
        } else {
            handler = requestPostMap.get(requestURI);
        }
        // 핸들러가 존재 하지 않는 경우, 게시글 목록으로 조회합니다.
        if (handler == null) {
            handler = new BoardListServlet();
        }

        // 핸들러를 처리할 수 있는 어댑터를 조회합니다.
        MyAdapter handlerAdapter = null;
        for (MyAdapter adapter : adapters) {
            if (adapter.isProcess(handler)) {
                handlerAdapter = adapter;
            }
        }

        if (handlerAdapter == null) {
            throw new HandlerAdapterNotFoundException();
        }

        // 서블릿 호출 결과 뷰와 모델을 담고 있습니다.
        MyModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        String logicalViewPath = modelAndView.getLogicalViewPath();

        // 응답 뷰가 없는 경우 파일다운로드 또는 댓글로 판단, 정상적으로 종료합니다.
        if (logicalViewPath == null) {
            return;
        }

        // 리다이렉트인지 확인 하여 경로를 추출하여 리다이렉트 합니다.
        if (isRedirectPath(logicalViewPath)) {
            response.sendRedirect(getRedirectPath(logicalViewPath));
            return;
        }

        // 뷰리졸버를 통해 논리적인 뷰 경로를 실제 물리적인 뷰 경로를 포함한 뷰 객체를 반환합니다.
        MyView myView = JspViewResolver.viewResolve(modelAndView.getLogicalViewPath());
        // 뷰를 통해 실제 뷰(JSP)를 호출 합니다.
        myView.render(modelAndView.getModel(), request, response);
    }

    /**
     * GET 요청에 대한 핸들러를 초기화 합니다.
     */
    private void initRequestGetMap() {
        requestGetMap.put("/board", new BoardListServlet());
        requestGetMap.put("/board/register", new BoardRegisterFormServlet());
        requestGetMap.put("/board/board", new BoardServlet());
        requestGetMap.put("/board/update", new BoardUpdateFormServlet());
        requestGetMap.put("/board/fileDown", new FileDownServlet());
    }

    /**
     * POST 요청에 대한 핸들러를 초기화 합니다.
     */
    private void initRequestPostMap() {
        requestPostMap.put("/board/register", new BoardRegisterServlet());
        requestPostMap.put("/board/update", new BoardUpdateServlet());
        requestPostMap.put("/board/delete", new BoardDeleteServlet());
        requestPostMap.put("/board/reply", new BoardReplyServlet());
    }


    /**
     * 어댑터 목록을 초기화 합니다.
     */
    private void initAdapters() {
        adapters.add(new ServletAdapter());
    }

    /**
     * 제공된 뷰 경로가 리다이렉트를 나타내는지 확인합니다.
     * @param viewPath 확인할 뷰 경로
     * @return 뷰 경로가 리다이렉트를 나타내는 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     */
    private boolean isRedirectPath(String viewPath) {
        return viewPath.startsWith(redirectPath);
    }

    /**
     * 리다이렉트 경로가 주어지면 실제 리다이렉트 URL을 반환합니다.
     *
     * @param viewPath 리다이렉트 경로
     * @return 실제 리다이렉트 URL
     */
    private String getRedirectPath(String viewPath) {
        return viewPath.replace(redirectPath, "");
    }
}