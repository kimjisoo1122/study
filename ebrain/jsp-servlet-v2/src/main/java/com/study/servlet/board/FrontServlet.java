package com.study.servlet.board;

import com.study.exception.HandlerAdapterNotFoundException;
import com.study.servlet.MyModelAndView;
import com.study.servlet.MyView;
import com.study.servlet.adapter.MyAdapter;
import com.study.servlet.adapter.ServletAdapter;
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

@WebServlet("/board/*")
public class FrontServlet extends HttpServlet {

    private final String REDIRECT_PATH = "redirect:";

    private final Map<String, Object> requestGetMap = new HashMap<>();
    private final Map<String, Object> requestPostMap = new HashMap<>();
    private final List<MyAdapter> adapters = new ArrayList<>();

    /**
     * requestMapping을 등록한다.
     */
    public FrontServlet() {
        initRequestGetMap();
        initRequestPostMap();
        initAdapters();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
            requestMap에서 요청된 uri에 해당하는 handler 반환
            adapeter가 handler를 처리할 수 있는 경우
            request에 들어온 파라미터를 파싱하고 모델을 생성하여 실제 servlet에 전달
            servlet의 반환된 값으로 ModelAndView를 생성하여 프론트서블릿에 반환
            프론트서블릿은 viewResolver를 통해 실제 경로를 가지고 있는 view 리턴
            view는 model and view에서 model을 request에 다시 담아 실제 view경로로 포워딩
         */

        // requestMap 조회
        String requestURI = request.getRequestURI();
        Object handler = null;
        // get or post
        if (request.getMethod().equalsIgnoreCase("get")) {
            handler = requestGetMap.get(requestURI);
        } else {
            handler = requestPostMap.get(requestURI);
        }
        // 요청한 URI가 잘못된 경우 게시글 목록 handler 리턴
        if (handler == null) {
            handler = new BoardListServlet();
        }

        // 어뎁터는 request를 파싱하고 모델을 생성하여 servlet을 호출하고8
        // ModelAndView를 프론트서블릿에 반환 한다.
        MyModelAndView modelAndView = null;
        for (MyAdapter adapter : adapters) {
            if (adapter.isProcess(handler)) {
                modelAndView = adapter.handle(request,response, handler);
            }
        }

        if (modelAndView == null) {
            throw new HandlerAdapterNotFoundException();
        }


        String logicalViewPath = modelAndView.getLogicalViewPath();
        // 응답 View 없는 경우 (파일 다운로드, 댓글)
        if (logicalViewPath == null) {
            return;
        }

        // 리다이렉트
        if (isRedirectPath(logicalViewPath)) {
            response.sendRedirect(getRedirectPath(logicalViewPath));
            // 리다이렉트는 그 즉시 페이지를 이동하는게 아니라
            // 리다이렉트 이후 response header에 이동할 url을 반환하고 로직을 이어간다.
            return;
        }

        // 뷰리졸버를 통해 MyView를 반환합니다.
        MyView myView = JspViewResolver.viewResolve(modelAndView.getLogicalViewPath());
        // render를 통해 해당 view로 포워딩 합니다.
        myView.render(modelAndView.getModel(), request, response);
    }

    /**
     * GET requestMap을 초기화 합니다.
     */
    private void initRequestGetMap() {
        requestGetMap.put("/board", new BoardListServlet());
        requestGetMap.put("/board/register", new BoardRegisterFormServlet());
        requestGetMap.put("/board/board", new BoardServlet());
        requestGetMap.put("/board/update", new BoardUpdateFormServlet());
        requestGetMap.put("/board/fileDown", new FileDownServlet());
        // POST
    }

    /**
     * POST requestMap을 초기화 합니다.
     */
    private void initRequestPostMap() {
        requestPostMap.put("/board/register", new BoardRegisterServlet());
        requestPostMap.put("/board/update", new BoardUpdateServlet());
        requestPostMap.put("/board/delete", new BoardDeleteServlet());
        requestPostMap.put("/board/reply", new BoardReplyServlet());
    }


    /**
     * adapters 초기화 합니다.
     */
    private void initAdapters() {
        adapters.add(new ServletAdapter());
    }

    /**
     * 반환된 viewPath가 redirect인지 확인합니다.
     * @param viewPath
     * @return
     */
    private boolean isRedirectPath(String viewPath) {
        return viewPath.startsWith(REDIRECT_PATH);
    }

    /**
     * 리다이렉트 path인 경우 리다이렉트 URL 반환
     * @param viewPath
     * @return
     */
    private String getRedirectPath(String viewPath) {
        return viewPath.replace(REDIRECT_PATH, "");
    }
}