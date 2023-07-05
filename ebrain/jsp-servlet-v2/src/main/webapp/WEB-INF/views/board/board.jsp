<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.dto.ReplyDto" %>
<%@ page import="com.study.dto.BoardSearchCondition" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>

<%
  BoardDto board = (BoardDto) request.getAttribute("board");
  BoardSearchCondition condition = (BoardSearchCondition) request.getAttribute("condition");
  List<FileDto> files = (List<FileDto>) request.getAttribute("files");
  List<ReplyDto> replies = (List<ReplyDto>) request.getAttribute("replies");

%>

<html>

  <head>
    <title>게시글</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/board.css">
    <script src="${pageContext.request.contextPath}/resources/js/board/board.js"></script>
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
                    onclick="replyRegister(<c:out value="<%=board.getBoardId()%>"/>)">등록
            </button>
        </div>
      </div>

      <div class="button-container">
        <button class="button-list">
          <c:url value="/board" var="boardListUrl">
            <c:param name="page" value="<%=String.valueOf(condition.getPage())%>"/>
            <%@include file="condition/conditionParam.jsp"%>
          </c:url>
          <a href="${boardListUrl}" class="button-list-a">목록</a>
        </button>
        <button class="button-update">
          <c:url value="/board/update" var="updateUrl">
            <c:param name="page" value="<%=String.valueOf(condition.getPage())%>"/>
            <c:param name="boardId" value="<%=String.valueOf(board.getBoardId())%>"/>
            <%@include file="condition/conditionParam.jsp"%>
          </c:url>
          <a href="${updateUrl}" class="button-update-a">수정</a>
        </button>
        <button type="button"
                class="button-remove"
                onclick="removeOpen()">삭제
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
              action="/board/delete"
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
          <input type="hidden" name="search" value="<c:out value="<%=condition.getSearch()%>"/>">
          <input type="hidden" name="fromDate" value="<c:out value="<%=condition.getFromDate()%>"/>">
          <input type="hidden" name="toDate" value="<c:out value="<%=condition.getToDate()%>"/>">
          <input type="hidden" name="categoryId" value="<c:out value="<%=condition.getSearchCategory()%>"/>">
          <input type="hidden" name="page" value="<c:out value="<%=String.valueOf(condition.getPage())%>"/>">

        </form>
      </div>
    </div>

  </body>

</html>
