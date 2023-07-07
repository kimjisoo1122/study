package com.study.servlet.board;

import com.google.gson.Gson;
import com.study.dto.ReplyDto;
import com.study.service.ReplyService;
import com.study.servlet.MyServlet;
import com.study.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class BoardReplyServlet implements MyServlet {

    private final ReplyService replyService = ReplyService.getReplyService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        HttpServletResponse response = (HttpServletResponse) model.get("response");
        // 파라미터 파싱
        String content = paramMap.get("content");
        String boardIdParam = paramMap.get("boardId");
        if (!StringUtil.isNumeric(boardIdParam)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);;
        }

        // 댓글 생성
        ReplyDto replyDto = new ReplyDto();
        replyDto.setBoardId(Long.valueOf(boardIdParam));
        replyDto.setReplyContent(content);

        // 댓글 조회 (댓글 생성하면 등록한 댓글 반환)
        ReplyDto reply = replyService.register(replyDto);

        // 댓글객체 -> json 응답
        Gson gson = new Gson();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(reply));
        return null;
    }
}
