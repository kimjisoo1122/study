<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.dto.BoardSearchCondition" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%
  BoardDto board = (BoardDto) request.getAttribute("board");
  List<FileDto> files = (List<FileDto>) request.getAttribute("files");
  BoardSearchCondition condition = (BoardSearchCondition) request.getAttribute("condition");
  String fileError = (String) Optional.ofNullable(request.getAttribute("fileError")).orElse(null);
%>

<html>

  <head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/update.css">
    <script src="${pageContext.request.contextPath}/resources/js/board/update.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/board/boardValidation.js"></script>
  </head>

  <body>
    <form method="post"
          enctype="multipart/form-data"
          accept-charset="UTF-8"
          onsubmit="return validForm()">

      <div class="update-container">

          <div class="category-container">
            <div class="category-title">카테고리
              <span class="required-star">*</span>
            </div>
            <div class="category-name"><c:out value="<%=board.getCategoryName()%>"/></div>
          </div>

          <div class="create-date-container">
            <div class="create-title">등록 일시</div>
            <div class="create-date"><c:out value="<%=board.getFormattedCreateDate()%>"/></div>
          </div>

          <div class="update-date-container">
            <div class="update-title">수정 일시</div>
            <div class="update-date"><c:out value="<%=board.getFormattedUpdateDate()%>"/></div>
          </div>

          <div class="view-container">
            <div class="view-title">조회수</div>
            <div class="view-cnt"><c:out value="<%=board.getViewCnt()%>"/></div>
          </div>

          <div class="writer-container">
            <div class="writer-title">작성자
              <span class="required-star">*</span>
            </div>
            <div class="writer-input-container">
              <input type="text"
                     class="writer-input"
                     value="<c:out value="<%=board.getWriter()%>"/>"
                     name="writer"
                     onchange="validWriter()">
              <span class="writer-input-error">작성자 에러</span>
            </div>
          </div>

          <div class="password-container">
            <div class="password-title">비밀번호</div>
            <div class="password-input-container">
              <input type="password"
                     class="password-input"
                     name="password"
                     placeholder="비밀번호"
                     onchange="validPassword()">
              <span class="password-input-error">비밀번호 에러</span>
            </div>
          </div>

          <div class="title-container">
            <div class="title-title">제목
              <span class="required-star">*</span>
            </div>
            <div class="title-input-container">
              <input type="text"
                     class="title-input"
                     value="<c:out value="<%=board.getTitle()%>"/>"
                     name="title"
                     onchange="validTitle()">
              <span class="title-input-error">제목 에러</span>
            </div>
          </div>

          <div class="content-container">
            <div class="content-title">내용
              <span class="required-star">*</span>
            </div>
            <div class="content-text-container">
              <textarea class="content-text"
                        name="content"
                        onchange="validContent()"><c:out value="<%=board.getContent()%>"/></textarea>
              <span class="content-text-error">내용 에러</span>
            </div>
          </div>

          <div class="file-list-container">
            <div class="file-title">파일 첨부</div>
            <div class="file-input-container">
              <c:forEach items="<%=files%>" var="file">
                <div class="file-container">
                  <div class="file-name">${file.physicalName}</div>
                  <button class="file-download">
                    <a href="fileDown.jsp?fileId=${file.fileId}" class="file-download-a">Download</a>
                  </button>
                  <button class="file-cancel"
                          type="button"
                          onclick="deleteFile(this, ${file.fileId})"> X
                  </button>
                </div>
              </c:forEach>
              <c:forEach begin="1" end="<%=3 - files.size()%>" varStatus="statue">
                <div class="file-register-container">
                  <input type="text"
                         class="file-disabled"
                         value="<%=fileError == null ? "" : fileError%>"
                         style="<%=fileError == null ? "color : black" : "color : red"%>"
                         disabled>
                  <label for="file${statue.index}" class="file-input-label">파일 찾기</label>
                  <input type="file"
                         id="file${statue.index}"
                         name="file${statue.index}"
                         class="file-input"
                         onchange="uploadFile(this)">
                </div>
              </c:forEach>
            </div>
          </div>

          <div class="button-container">
            <button type="button" class="button-cancel">
              <c:url value="/board/board" var="boardUrl">
                <c:param name="page" value="<%=String.valueOf(condition.getPage())%>"/>
                <c:param name="boardId" value="<%=String.valueOf(board.getBoardId())%>"/>
                <%@include file="condition/conditionParam.jsp"%>
              </c:url>
              <a href="${boardUrl}" class="button-cancel-a">취소</a>
            </button>
            <button type="submit" class="button-save">저장</button>
          </div>

      </div>

      <%--검색조건 유지--%>
      <input type="hidden" name="boardId" value="<c:out value="<%=board.getBoardId()%>"/>">
      <%@include file="condition/conditionHidden.jsp"%>

    </form>

  </body>

</html>

<%
  session.removeAttribute("board");
  session.removeAttribute("fileError");
%>