package com.study.servlet.board;

import com.google.gson.Gson;
import com.study.dao.ReplyDao;
import com.study.dto.ReplyDto;
import com.study.servlet.ServletHandler;
import com.study.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReplyRegisterHandler implements ServletHandler {
    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 파라미터 파싱
        String content = request.getParameter("content");
        String boardIdStr = request.getParameter("boardId");
        if (!StringUtil.isNumeric(boardIdStr)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);;
        }

        // 댓글 생성
        ReplyDto replyDto = new ReplyDto();
        replyDto.setBoardId(Long.valueOf(boardIdStr));
        replyDto.setReplyContent(content);
        ReplyDao replyDao = new ReplyDao();
        Long registerId = replyDao.register(replyDto);

        // 댓글 조회
        ReplyDto reply = replyDao.findById(registerId);

        // 댓글객체 -> json 응답
        Gson gson = new Gson();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(reply));
    }
}
