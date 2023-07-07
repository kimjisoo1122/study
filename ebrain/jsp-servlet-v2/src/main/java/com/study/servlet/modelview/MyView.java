package com.study.servlet.modelview;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * ViewResolver를 통해 실제 View경로를 포함한 객체
 */
public class MyView {

    private final String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    /**
     * 실제 View로 forward 합니다.
     * @param model View에 전달 될 값
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.forEach(request::setAttribute);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}