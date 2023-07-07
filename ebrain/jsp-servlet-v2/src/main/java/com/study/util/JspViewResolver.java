package com.study.util;

import com.study.servlet.modelview.MyView;

public interface JspViewResolver {

    String PREFIX = "/WEB-INF/views/";
    String SUFFIX = ".jsp";

    /**
     * jsp 경로를 반환합니다.
     * @param logicalViewPath
     * @return physicalViewPath 실제 물리적 경로
     */
    static String getPhysicalViewPath(String logicalViewPath) {
        logicalViewPath = logicalViewPath.charAt(0) == '/'
                ? logicalViewPath.substring(1)
                : logicalViewPath;
        return PREFIX + logicalViewPath + SUFFIX;
    }


    /**
     * 뷰의 논리경로를 실제 물리경로로 변환하여 MyView반환
     * @param logicalViewPath 뷰의 논리경로
     * @return MyView
     */
    static MyView viewResolve(String logicalViewPath) {
        String physicalViewPath = getPhysicalViewPath(logicalViewPath);
        return new MyView(physicalViewPath);

    }
}
