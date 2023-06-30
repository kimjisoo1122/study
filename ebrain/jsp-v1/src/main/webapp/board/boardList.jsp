<%@ page import="com.study.dao.BoardDao" %>
<%@ page import="com.study.dto.BoardSearchCondition" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.dao.CategoryDao" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.study.dto.CategoryDto" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.study.page.PageHandler" %>
<%@ page import="com.study.dao.FileDao" %>
<%@ page import="com.study.util.StringUtil" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  request.setCharacterEncoding("utf-8");
  // 웹페이지 뒤로가기 캐시 방지 (조회수 반영)
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

  // 검색조건 파싱
  BoardSearchCondition condition = new BoardSearchCondition();
  String fromDate = StringUtil.nvl(request.getParameter("fromDate")) ;
  String toDate = StringUtil.nvl(request.getParameter("toDate"));
  String search = StringUtil.nvl(request.getParameter("search"));
  String categoryId = StringUtil.nvl(request.getParameter("categoryId"));
  String pageNumStr = request.getParameter("page");


  // 검색조건 설정
  if (!categoryId.isEmpty()) {
    condition.setCategoryId(Long.parseLong(categoryId));
  }
  condition.setFromDate(fromDate);
  condition.setToDate(toDate);
  condition.setSearch(search);

  // 게시글 조회
  BoardDao boardDao = new BoardDao();
  List<BoardDto> boards = boardDao.findAll(condition);

  // 게시글 총 갯수 조회
  int totalCnt = boards.size();
  int pageNum = pageNumStr == null ? 1 : Integer.parseInt(pageNumStr);
  int pageSize = 10;


  // 게시글 페이징 처리
  int offset = (pageNum - 1) * pageSize;
  boards = boards.stream()
              .skip(offset)
              .limit(pageSize)
              .collect(Collectors.toList());
  PageHandler pageHandler = new PageHandler(pageNum, totalCnt);

  // 첨부파일 여부 조회
  FileDao fileDao = new FileDao();
  boards.forEach(e -> {
    fileDao.findByBoardId(e.getBoardId()).stream()
            .findAny()
            .ifPresent(t -> e.setHasFile(true));
  });
%>


<html>
  <head>
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
                 value="<c:out value="<%=fromDate%>"/>">
          <span class="date-between"> ~ </span>
          <input type="date"
                 class="date-to"
                 name="toDate"
                 value="<c:out value="<%=toDate%>"/>">
        </div>
        <div class="condition-container">

          <%-- 카테고리 조회 --%>
          <%
            CategoryDao categoryDao = new CategoryDao();
            Set<Map.Entry<String, List<CategoryDto>>> categories = categoryDao.findAll().entrySet();
          %>

          <select name="categoryId" class="condition-category">
            <option value="">전체 카테고리</option>
            <c:forEach items="<%=categories%>" var="categoryMap">
              <optgroup label="${categoryMap.key}">
                <c:forEach items="${categoryMap.value}" var="category">
                <option value="${category.categoryId}"
                  <c:if test="${category.categoryId == param.get('categoryId')}">selected</c:if>>
                  ${category.name}
                </option>
                </c:forEach>
              </optgroup>
            </c:forEach>
          </select>
          <input type="text"
                 name="search"
                 class="condition-search"
                 value="<c:out value="<%=search%>"/>"
                 placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)">
          <button type="submit" class="condition-submit">검색</button>
        </div>
      </div>
    </form>

    <div class="total-container">
      <p class="total-board">총 <c:out value="<%=totalCnt%>"/> 건 </p>
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
            <c:url value="board.jsp" var="boardUrl">
              <c:param name="boardId" value="${board.boardId}"/>
              <c:param name="page" value="<%=String.valueOf(pageHandler.getPage())%>"/>
              <c:param name="search" value="<%=search%>"/>
              <c:param name="categoryId" value="<%=categoryId%>"/>
              <c:param name="fromDate" value="<%=fromDate%>"/>
              <c:param name="toDate" value="<%=toDate%>"/>
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
          <c:url value="boardList.jsp" var="prevTotalPage">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getBeginPage() - 1)%>"/>
          </c:url>
          <a href="${prevTotalPage}" class="paging-prev-total"><<</a>
        </c:if>
      </div>

      <div class="paging-prev-container">
        <c:if test="<%=pageHandler.getPage() != pageHandler.getBeginPage()%>">
          <c:url value="boardList.jsp" var="prevPage">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getPage() - 1)%>"/>
          </c:url>
          <a href="${prevPage}" class="paging-prev"><</a>
        </c:if>
      </div>

      <div class="paging-index-container">
        <c:forEach begin="<%=pageHandler.getBeginPage()%>" end="<%=pageHandler.getMaxPage()%>" varStatus="status">
          <c:url value="boardList.jsp" var="nowPage">
            <c:param name="page" value="${status.index}"/>
          </c:url>
          <a href="${nowPage}" class="paging-page">${status.index}</a>
        </c:forEach>
      </div>

      <div class="paging-next-container">
        <c:if test="<%=pageHandler.getPage() != pageHandler.getEndPage()%>">
          <c:url value="boardList.jsp" var="nextPage">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getPage() + 1)%>"/>
          </c:url>
          <a href="${nextPage}" class="paging-next">></a>
        </c:if>
      </div>

      <div class="paging-next-total-container">
        <c:if test="<%=pageHandler.isNext()%>">
          <c:url value="boardList.jsp" var="nextTotalPage">
            <c:param name="page" value="<%=String.valueOf(pageHandler.getMaxPage() + 1)%>"/>
          </c:url>
          <a href="${nextTotalPage}" class="paging-next-total">>></a>
        </c:if>
      </div>


    </div>

    <div class="board-register-container">
      <button class="board-register-button">
        <c:url value="register.jsp" var="register">
          <c:param name="page" value="<%=String.valueOf(pageHandler.getPage())%>"/>
          <c:param name="search" value="<%=search%>"/>
          <c:param name="categoryId" value="<%=categoryId%>"/>
          <c:param name="fromDate" value="<%=fromDate%>"/>
          <c:param name="toDate" value="<%=toDate%>"/>
        </c:url>
        <a href="${register}">등록</a>
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
