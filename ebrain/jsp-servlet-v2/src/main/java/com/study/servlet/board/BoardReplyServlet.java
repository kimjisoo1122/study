package com.study.servlet.board;

import com.google.gson.Gson;
import com.study.dto.ReplyDto;
import com.study.service.ReplyService;
import com.study.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 게시글의 댓글을 등록하는 서블릿 입니다.
 */
public class BoardReplyServlet implements MyServlet {

    private final ReplyService replyService = ReplyService.getReplyService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        HttpServletResponse response = (HttpServletResponse) model.get("response");
        String content = paramMap.get("content");
        Long boardId = StringUtil.toLong(paramMap.get("boardId"));
        // 게시글번호가 유효하지 않으면 Error를 보냅니다.
        if (boardId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        // 댓글을 생성합니다.
        ReplyDto replyDto = new ReplyDto();
        replyDto.setBoardId(boardId);
        replyDto.setReplyContent(content);

        // 댓글을 조회한 후 생성된 댓글을 반환합니다.
        ReplyDto reply = replyService.register(replyDto);

        // 댓글 객체를 JSON 형태로 변환하여 AJAX호출에 응답합니다.
        Gson gson = new Gson();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(reply));
        return null;
    }
}
