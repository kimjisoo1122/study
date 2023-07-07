package com.study.servlet.adapter;

import com.study.servlet.modelview.MyModelAndView;
import com.study.servlet.board.MyServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet을 실행시켜 ModelAndView를 반환합니다.
 */
public class ServletAdapter implements MyAdapter {

    @Override
    public boolean isProcess(Object handler) {
        return handler instanceof MyServlet;
    }

    @Override
    public MyModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        MyServlet servlet = (MyServlet) handler;

        Map<String, String> paramMap = extractParamMap(request);

        MyModelAndView modelAndView = new MyModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        // reqeust가 필요한(multipart request)경우 request를 사용한다.
        model.put("request", request);
        model.put("response", response);
        String logicalViewPath = servlet.handle(paramMap, model);
        modelAndView.setLogicalViewPath(logicalViewPath);

        return modelAndView;
    }
}
