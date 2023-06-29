<%@ page import="com.study.dao.ReplyDao" %>
<%@ page import="com.study.dto.ReplyDto" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
  // 댓글 등록
  request.setCharacterEncoding("utf-8");
  Long boardId = Long.parseLong(request.getParameter("boardId"));
  String content = request.getParameter("content");
  ReplyDto replyDto = new ReplyDto();
  replyDto.setBoardId(boardId);
  replyDto.setContent(content);

  ReplyDao replyDao = new ReplyDao();
  Long replyId = replyDao.register(replyDto);

  // 등록한 댓글 조회
  ReplyDto reply = replyDao.findById(replyId);

  // 객체를 json형태로 변환하여 전송
  Gson gson = new Gson();
  response.setContentType("application/json");
  response.getWriter().write(gson.toJson(reply));
%>