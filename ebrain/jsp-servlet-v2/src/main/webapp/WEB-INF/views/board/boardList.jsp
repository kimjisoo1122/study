<%@ page import="com.study.dto.BoardSearchCondition" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.study.dto.CategoryDto" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.study.page.PageHandler" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  List<BoardDto> boards = (List<BoardDto>) request.getAttribute("boards");
  PageHandler pageHandler = (PageHandler) request.getAttribute("pageHandler");
  BoardSearchCondition condition = (BoardSearchCondition) request.getAttribute("condition");
  Set<Map.Entry<String, List<CategoryDto>>> categories =
          (Set<Map.Entry<String, List<CategoryDto>>>) request.getAttribute("categories");
%>

<html>
  <head>
    <meta charset="UTF-8">
    <title>게시글 목록</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/boardList.css">
    <script src="${pageContext.request.contextPath}/resources/js/board/boardList.js"></script>
  </head>
  <body>

  <div class="boardlist-container">

    <form method="get" class="search-form">
      <div class="search-container">
        <div class="date-container">
          <p class="date-register">등록일</p>
          <input type="date"
                 class="date-from"
                 name="fromDate"
                 value="<c:out value="<%=condition.getFromDate()%>"/>">
          <span class="date-between"> ~ </span>
          <input type="date"
                 class="date-to"
                 name="toDate"
                 value="<c:out value="<%=condition.getToDate()%>"/>">
        </div>
        <div class="condition-container">

          <select name="searchCategory" class="condition-category">
            <option value="">전체 카테고리</option>
            <c:forEach items="<%=categories%>" var="categoryMap">
              <optgroup label="${categoryMap.key}">
                <c:forEach items="${categoryMap.value}" var="category">
                <option value="${category.categoryId}"
                  <c:if test="${category.categoryId == param.get('searchCategory')}">selected</c:if>>
                  ${category.categoryName}
                </option>
                </c:forEach>
              </optgroup>
            </c:forEach>
          </select>
          <input type="text"
                 name="search"
                 class="condition-search"
                 value="<c:out value="<%=condition.getSearch()%>"/>"
                 placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)">
          <button type="submit" class="condition-submit">검색</button>
        </div>
      </div>
    </form>

    <div class="total-container">
      <p class="total-board">총 <c:out value="<%=pageHandler.getTotalCnt()%>"/> 건 </p>
    </div>

    <div class="list-container">
      <ul class="list-header">
        <li class="list-category">카테고리</li>
        <li class="list-title">제목</li>
        <li class="list-writer">작성자</li>
        <li class="list-view">조회수</li>
        <li class="list-create">등록 일시</li>
        <li class="list-update">수정 일시</li>
      </ul>

      <c:forEach items="<%=boards%>" var="board">
        <div class="list-board-container">
          <div class="list-category">${board.categoryName}</div>
          <div class="list-title">
            <c:if test="${board.hasFile}">
              <span class="list-title-file">File</span>
            </c:if>
            <c:url value="/board/board" var="boardUrl">
              <c:param name="boardId" value="${board.boardId}"/>
              <c:param name="page" value="<%=String.valueOf(pageHandler.getPage())%>"/>
              <%@include file="condition/conditionParam.jsp"%>
            </c:url>
            <a href="${boardUrl}" class="list-title-a">${board.title}</a>
          </div>
          <div class="list-writer">${board.writer}</div>
          <div class="list-view">${board.viewCnt}</div>
          <div class="list-create">${board.formattedCreateDate}</div>
          <div class="list-update">${board.formattedUpdateDate}</div>
        </div>
      </c:forEach>
    </div>

    <div class="paging-container" >
      <div class="paging-prev-total-container">
        <c:if test="<%=pageHandler.isPrevious()%>">
          <c:url value="/board" var="prevTotalPageUrl">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getBeginPage() - 1)%>"/>
            <%@include file="condition/conditionParam.jsp"%>
          </c:url>
          <a href="${prevTotalPageUrl}" class="paging-prev-total"><<</a>
        </c:if>
      </div>

      <div class="paging-prev-container">
        <c:if test="<%=pageHandler.getTotalCnt() != 0 && pageHandler.getPage() != pageHandler.getBeginPage()%>">
          <c:url value="/board" var="prevPageUrl">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getPage() - 1)%>"/>
            <%@include file="condition/conditionParam.jsp"%>
          </c:url>
          <a href="${prevPageUrl}" class="paging-prev"><</a>
        </c:if>
      </div>

      <div class="paging-index-container">
        <c:forEach begin="<%=pageHandler.getBeginPage()%>" end="<%=pageHandler.getMaxPage()%>" varStatus="status">
          <c:url value="/board" var="nowPageUrl">
            <c:param name="page" value="${status.index}"/>
            <%@include file="condition/conditionParam.jsp"%>
          </c:url>
          <a href="${nowPageUrl}" class="paging-page">${status.index}</a>
        </c:forEach>
      </div>

      <div class="paging-next-container">
        <c:if test="<%=pageHandler.getTotalCnt() != 0 && pageHandler.getPage() != pageHandler.getEndPage()%>">
          <c:url value="/board" var="nextPageUrl">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getPage() + 1)%>"/>
            <%@include file="condition/conditionParam.jsp"%>
          </c:url>
          <a href="${nextPageUrl}" class="paging-next">></a>
        </c:if>
      </div>

      <div class="paging-next-total-container">
        <c:if test="<%=pageHandler.isNext()%>">
          <c:url value="/board" var="nextTotalPageUrl">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getMaxPage() + 1)%>"/>
            <%@include file="condition/conditionParam.jsp"%>
          </c:url>
          <a href="${nextTotalPageUrl}" class="paging-next-total">>></a>
        </c:if>
      </div>


    </div>

    <div class="board-register-container">
      <button class="board-register-button">
        <c:url value="/board/register" var="registerFormUrl">
          <c:param name="page" value="<%=String.valueOf(pageHandler.getPage())%>"/>
          <%@include file="condition/conditionParam.jsp"%>
        </c:url>
        <a href="${registerFormUrl}">등록</a>
      </button>
    </div>

  </div>

  <%-- 게시글 삭제시 alert 호출 --%>
  <c:if test="${removeMsg != null}">
    <script>
      alert('${removeMsg}');
    </script>
    <%
      session.removeAttribute("removeMsg");
    %>
  </c:if>

  </body>

</html>
