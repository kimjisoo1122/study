<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.study.dao.CategoryDao" %>
<%@ page import="com.study.dto.CategoryDto" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.dto.BoardSearchCondition" %>
<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  BoardSearchCondition condition = (BoardSearchCondition) request.getAttribute("condition");
  BoardDto board = (BoardDto) Optional.ofNullable(request.getAttribute("board")).orElse(null);
  String fileError = (String) Optional.ofNullable(request.getAttribute("fileError")).orElse(null);
%>

<html>
  <head>
    <meta charset=UTF-8">
    <title>글 작성</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/register.css">
    <script src="${pageContext.request.contextPath}/resources/js/board/register.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/board/boardValidation.js"></script>
  </head>

    <body>

      <form method="post"
            enctype="multipart/form-data"
            accept-charset="UTF-8"
            onsubmit="return validForm()">

        <div class="register-container">

          <div class="category-container">
            <div class="category-title">카테고리
              <span class="required-star">*</span>
            </div>
            <div class="category-select-container">
              <%-- 카테고리 조회 --%>
              <%
                CategoryDao categoryDao = new CategoryDao();
                Set<Map.Entry<String, List<CategoryDto>>> categories = categoryDao.findAll().entrySet();
              %>
              <select name="categoryId" class="category-select">
                <c:forEach items="<%=categories%>" var="categoryMap">
                  <optgroup label="${categoryMap.key}">
                    <c:forEach items="${categoryMap.value}" var="category">
                      <option value="${category.categoryId}">${category.categoryName}</option>
                    </c:forEach>
                  </optgroup>
                </c:forEach>

              </select>
            </div>
          </div>

          <div class="writer-container">
            <div class="writer-title">작성자
              <span class="required-star">*</span>
            </div>
            <div class="writer-input-container">
              <input type="text"
                     name="writer"
                     class="writer-input"
                     value="<%=board == null ? "" : board.getWriter()%>"
                     onchange="validWriter()">
              <span class="writer-input-error">작성자 에러</span>
            </div>
          </div>

          <div class="password-container">
            <div class="password-title">비밀번호
              <span class="required-star">*</span>
            </div>
            <div class="password-input-container">
              <input type="password"
                     name="password"
                     class="password-input"
                     onchange="validPassword()">
              <input type="password"
                     class="password-input-confirm"
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
                     name="title"
                     class="title-input"
                     value="<%=board == null ? "" : board.getTitle()%>"
                     onchange="validTitle()">
              <span class="title-input-error">제목 에러</span>
            </div>
          </div>

          <div class="content-container">
            <div class="content-title">내용
              <span class="required-star">*</span>
            </div>
            <div class="content-text-container">
              <textarea name="content"
                        class="content-text"
                        onchange="validContent()"><%=board == null ? "" : board.getContent()%></textarea>
              <span class="content-text-error">내용 에러</span>
            </div>
          </div>

          <div class="file-container">
            <div class="file-title">파일 첨부</div>
            <div class="file-list-container">
                <c:forEach begin="1" end="3" varStatus="status">
                  <div class="file-input-container">
                    <input type="text"
                           class="file-input"
                           disabled>
                    <label for="file${status.index}">파일 찾기</label>
                    <input type="file"
                           name="file${status.index}"
                           id="file${status.index}"
                           onchange="uploadFile(this)">
                  </div>
                </c:forEach>
              <c:if test="<%=fileError != null%>">
                <span class="file-error"><c:out value="<%=fileError%>"/></span>
              </c:if>

            </div>
          </div>

          <div class="button-container">
            <button type="button" class="button-cancel">
              <c:url value="boardList.jsp" var="boardListUrl">
                <c:param name="page" value="<%=String.valueOf(condition.getPage())%>"/>
                <%@include file="condition/conditionParam.jsp"%>
              </c:url>
              <a href="${boardListUrl}" class="button-cancel-a">취소</a>
            </button>
            <button type="submit" class="button-save">저장</button>
          </div>

        </div>

        <%--검색조건--%>
        <%@include file="condition/conditionHidden.jsp"%>

      </form>

    </body>
</html>

<%
  session.removeAttribute("board");
  session.removeAttribute("fileError");
%>

