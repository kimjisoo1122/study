package com.study.util;

public interface JspViewResolver {

    /**
     * jsp 경로를 반환합니다.
     * @param path
     * @return view 경로
     */
    static String getViewPath(String path) {
        path = path.charAt(0) == '/' ? path : "/" + path;
        return "/WEB-INF/views" + path + ".jsp";
    }
}
