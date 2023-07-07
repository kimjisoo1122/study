package com.study.servlet.adapter;

import com.study.servlet.MyModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public interface MyAdapter {

    /**
     * 해당 handler를 처리할 수 있는 확인합니다.
     * @param handler
     * @return
     */
    boolean isProcess(Object handler);

    /**
     * Servlet을 실행하여 MyModelAndView 반환합니다.
     * @param request
     * @param response
     * @param handler
     * @return MyModelAndView
     */
    MyModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException;

    /**
     * request의 파라미터를 추출하여 map으로 반환합니다.
     * @param request
     * @return Map<String, String> paramMap
     */
    default Map<String, String> extractParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            paramMap.put(paramName, request.getParameter(paramName));
        }
        return paramMap;
    }
}
