<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.study.dao.BoardDao" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.study.dao.FileDao" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.util.StringUtil" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.File" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.study.dao.ReplyDao" %>
<%@ page import="com.study.dto.ReplyDto" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>

<%
  // 검색 조건
  String search = StringUtil.nvl(request.getParameter("search"));
  String fromDate = StringUtil.nvl(request.getParameter("fromDate"));
  String toDate = StringUtil.nvl(request.getParameter("toDate"));
  String categoryId = StringUtil.nvl(request.getParameter("categoryId"));
  String pageStr = StringUtil.nvl(request.getParameter("page"));
  String queryString = "?search=" + URLEncoder.encode(search) +
          "&categoryId=" + categoryId + "&fromDate=" + fromDate +
          "&toDate=" + toDate + "&page=" + pageStr;


  if (request.getMethod().equalsIgnoreCase("post")) {
    request.setCharacterEncoding("utf-8");

    if (request.getParameter("boardId") == null) {
        response.sendRedirect("/board/boardList.jsp" + queryString);
        return;
    }
    String password = StringUtil.nvl(request.getParameter("removePassword"));
    Long boardId = Long.parseLong(request.getParameter("boardId"));

    // 게시글 조회
    BoardDao boardDao = new BoardDao();
    BoardDto board = boardDao.findById(boardId);

    // 비밀번호 검증
    if (password.equals(board.getPassword())) {
      // 파일 삭제
      FileDao fileDao = new FileDao();
      List<FileDto> fileList = fileDao.findByBoardId(boardId);
      for (FileDto fileDto : fileList) {
        File regisetdFile = new File(fileDto.getPath() + File.separator + fileDto.getName());
        if (regisetdFile.exists()) {
          if (regisetdFile.delete()) {
            fileDao.delete(fileDto.getFileId());
          }
        }
      }
      // 댓글, 파일db, 게시글을 트랜잭션 내에서 삭제
      int deletedRowCnt = boardDao.deleteBoardAll(boardId);
      if (deletedRowCnt > 0) {
        session.setAttribute("removeMsg", "게시글을 삭제하였습니다.");
        response.sendRedirect("/board/boardList.jsp" + queryString);
        return;
      }
    }
    session.setAttribute("removeErrMsg", "게시글 삭제에 실패하였습니다.");
    String addQueryString = "&boardId=" + boardId;
    queryString += addQueryString;
    response.sendRedirect("/board/board.jsp" + queryString);

    return;
  } // 게시글 삭제 로직 종료

  // 게시글 조회 로직
  if (request.getParameter("boardId") == null) {
    response.sendRedirect("/board/boardList.jsp" + queryString);
    return;
  }
  Long boardId = Long.parseLong(request.getParameter("boardId"));
  BoardDao boardDao = new BoardDao();

  // 게시글 조회수 증가
  boardDao.addViewCnt(boardId);

  // 게시글 조회
  BoardDto board = boardDao.findById(boardId);

  // 첨부파일 조회
  FileDao fileDao = new FileDao();
  List<FileDto> files = fileDao.findByBoardId(boardId);

  // 댓글 조회
  ReplyDao replyDao = new ReplyDao();
  List<ReplyDto> replies = replyDao.findByBoardId(boardId);
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
            <p class="header-date-create">등록일시 <c:out value="<%=board.getFormattedCreateDate()%>"/></p>
            <p class="header-date-update">수정일시 <c:out value="<%=board.getFormattedUpdateDate()%>"/></p>
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
        <div class="reply-list-container">
          <c:forEach items="<%=replies%>" var="reply">
            <div class="reply">
              <div class="reply-date">${reply.formattedCreateDate}</div>
              <div class="reply-content">${reply.content}</div>
            </div>
          </c:forEach>
        </div>
        <div class="reply-register-container">
            <input type="text" name="content" class="reply-register-input" placeholder="댓글을 입력해 주세요.">
            <button
                    type="submit"
                    class="reply-register-button"
                    onclick="replyRegister(<c:out value="<%=boardId%>"/>)">등록
            </button>
        </div>
      </div>

      <div class="button-container">
        <button class="button-list">
          <c:url value="boardList.jsp" var="boardList">
            <c:param name="page" value="<%=pageStr%>"/>
            <c:param name="search" value="<%=search%>"/>
            <c:param name="categoryId" value="<%=categoryId%>"/>
            <c:param name="fromDate" value="<%=fromDate%>"/>
            <c:param name="toDate" value="<%=toDate%>"/>
          </c:url>
          <a href="${boardList}" class="button-list-a">목록</a>
        </button>
        <button class="button-update">
          <c:url value="update.jsp" var="update">
            <c:param name="page" value="<%=pageStr%>"/>
            <c:param name="search" value="<%=search%>"/>
            <c:param name="categoryId" value="<%=categoryId%>"/>
            <c:param name="fromDate" value="<%=fromDate%>"/>
            <c:param name="toDate" value="<%=toDate%>"/>
            <c:param name="boardId" value="<%=String.valueOf(boardId)%>"/>
          </c:url>
          <a href="${update}" class="button-update-a">수정</a>
        </button>
        <button type="button"
                onclick="removeOpen()">삭제
                class="button-remove"
        </button>
      </div>

    </div>

    <%-- 게시글 삭제실패시 alert 호출 --%>
    <c:if test="${removeErrMsg != null}">
      <script>
        alert('${removeErrMsg}');
      </script>
      <%
        session.removeAttribute("removeErrMsg");
      %>
    </c:if>

    <%-- 게시글 삭제 모달 --%>
    <div class="remove-modal-bg">
      <div class="remove-modal">
        <form method="post"
              onsubmit="return validRemovePassword()">
          <div class="remove-password-container">
            <div class="remove-password-title">비밀번호
              <span class="required-star">*</span>
            </div>
            <div class="remove-password-input-container">
              <input type="password"
                     name="removePassword"
                     class="remove-password-input"
                     onchange="validRemovePassword()">
              <span class="remove-password-input-error">비밀번호를 입력해주세요.</span>
            </div>
          </div>
          <div class="remove-button-container">
            <button type="button"
                    class="remove-button-cancel"
                    onclick="removeCancel()">취소
            </button>
            <button type="submit"
                    class="remove-button-submit">확인
            </button>
          </div>

          <%--검색조건--%>
          <input type="hidden" name="boardId" value="<c:out value="<%=board.getBoardId()%>"/>">
          <input type="hidden" name="search" value="<c:out value="<%=search%>"/>">
          <input type="hidden" name="fromDate" value="<c:out value="<%=fromDate%>"/>">
          <input type="hidden" name="toDate" value="<c:out value="<%=toDate%>"/>">
          <input type="hidden" name="categoryId" value="<c:out value="<%=categoryId%>"/>">
          <input type="hidden" name="page" value="<c:out value="<%=pageStr%>"/>">

        </form>
      </div>
    </div>

  </body>

</html>
