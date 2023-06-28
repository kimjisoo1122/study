<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.study.dao.BoardDao" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.study.dao.FileDao" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>

<%
  // 게시글 조회
  Long boardId = Long.parseLong(request.getParameter("boardId"));
  BoardDao boardDao = new BoardDao();
  BoardDto board = boardDao.findById(boardId);
  String createDate = board.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  String updateDate = board.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

  // 첨부파일 조회
  FileDao fileDao = new FileDao();
  List<FileDto> files = fileDao.findByBoardId(boardId);

%>

<html>
  <head>
    <title>게시글</title>
    <link rel="stylesheet" href="/resources/css/board/board.css">
    <script src="/resources/js/board/board.js"></script>
  </head>

  <body>

    <div class="board-container">

      <div class="header-container">
        <div class="header-top-container">

          <p class="header-name"><c:out value="<%=board.getWriter()%>"/></p>
          <div class="header-date-container">
            <p class="header-date-create">등록일시 <c:out value="<%=createDate%>"/></p>
            <p class="header-date-update">수정일시 <c:out value="<%=updateDate%>"/></p>
          </div>
        </div>

        <div class="header-bottom-container">
          <div class="header-title-container">
            <p>[<c:out value="<%=board.getCategoryName()%>"/>]</p>
            <h1 class="header-title"><c:out value="<%=board.getTitle()%>"/></h1>
          </div>
          <p class="header-view">조회수: <c:out value="<%=board.getViewCnt()%>"/></p>
        </div>
      </div>

      <div class="content-container">
        <p class="content-text"><c:out value="<%=board.getContent()%>"/></p>
      </div>

      <div class="file-container">
        <c:forEach items="<%=files%>" var="file">
          <a href="fileDown.jsp?fileId=${file.fileId}">${file.name}</a>
        </c:forEach>

      </div>

      <div class="reply-container">
        <div class="reply">
          <div class="reply-date">댓글등록일시</div>
          <div class="reply-content">댓글 내용</div>
        </div>
        <div class="reply-register-container">
          <form action="">
            <input type="text" name="content" class="reply-register-input">
            <button type="submit" class="reply-register-button">등록</button>
          </form>
        </div>
      </div>

      <div class="button-container">
        <button class="button-list">목록</button>
        <button class="button-update">수정</button>
        <button class="button-remove">삭제</button>
      </div>
    </div>




  </body>
</html>
