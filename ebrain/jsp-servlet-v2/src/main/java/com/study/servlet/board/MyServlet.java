package com.study.servlet.board;

import java.io.IOException;
import java.util.Map;

public interface MyServlet {

    /**
     * 클라이언트 요청을 처리합니다.
     * @param paramMap request param
     * @param model request에 담을 객체 model
     * @return 뷰의 논리경로
     */
    String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException;
}
