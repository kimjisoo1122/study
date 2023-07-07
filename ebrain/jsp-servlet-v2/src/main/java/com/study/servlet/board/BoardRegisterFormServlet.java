package com.study.servlet.board;

import com.study.dto.BoardSearchCondition;
import com.study.servlet.MyServlet;

import java.util.Map;

public class BoardRegisterFormServlet implements MyServlet {
    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) {
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        model.put("condition", condition);

        return "board/register";
    }
}
