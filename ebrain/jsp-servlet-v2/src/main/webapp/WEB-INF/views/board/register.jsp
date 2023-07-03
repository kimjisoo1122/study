<%@ page import="com.study.dao.BoardDao" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="com.study.dao.FileDao" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.study.dao.CategoryDao" %>
<%@ page import="com.study.dto.CategoryDto" %>
<%@ page import="com.study.validation.BoardValidation" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page import="com.study.util.StringUtil" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.study.util.FileUtil" %>
<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  if (request.getMethod().equalsIgnoreCase("post")) {
    request.setCharacterEncoding("utf-8");

    // MULTIPART 설정
    File uploadFolder = new File(FileUtil.FILE_PATH);
    if (!uploadFolder.exists()) {
      uploadFolder.mkdirs();
    }

    MultipartRequest multi = null;
    try {
      multi = new MultipartRequest(
                request, FileUtil.FILE_PATH, FileUtil.BOARD_FILE_MAX_SIZE, FileUtil.ENC_TYPE,
                new DefaultFileRenamePolicy());
    } catch (IOException e) {
        e.printStackTrace();
        // 파일사이즈 초과 에러메시지 전달
        session.setAttribute("fileError", "파일사이즈는 10MB를 넘을 수 없습니다.");
        response.sendRedirect("/board/register.jsp");
        return;
    }

    // 파라미터 파싱
    String writer = multi.getParameter("writer");
    String password = multi.getParameter("password");
    String title = multi.getParameter("title");
    String content = multi.getParameter("content");
    String categoryId = multi.getParameter("categoryId");

    // BoardDto 생성
    BoardDto boardDto = new BoardDto();
    if (categoryId != null) {
      boardDto.setCategoryId(Long.parseLong(categoryId));
    }
    boardDto.setTitle(title);
    boardDto.setWriter(writer);
    boardDto.setContent(content);
    boardDto.setPassword(password);

    // 게시글내 검색조건 유지
    String pageStr = StringUtil.nvl(request.getParameter("page"));
    String search = StringUtil.nvl(request.getParameter("search"));
    String fromDate = StringUtil.nvl(request.getParameter("fromDate"));
    String toDate = StringUtil.nvl(request.getParameter("toDate"));
    // 검색조건 쿼리스트링 생성
    String queryString = "?search=" + URLEncoder.encode(search) + "&categoryId=" + categoryId +
            "&fromDate=" + fromDate + "&toDate=" + toDate + "&page=" + pageStr;

    // 게시글 유효성 검증
    if (!BoardValidation.validBoard(boardDto)) {
      // 기존 입력 데이터 세션에 저장, 검색조건 쿼리스트링으로 전달
      session.setAttribute("board", boardDto);
      response.sendRedirect("/board/register.jsp" + queryString);
      return;
    }

    // 게시글 등록
    BoardDao boardDao = new BoardDao();
    Long boardId = boardDao.register(boardDto);

    // 파일 db 저장
    Enumeration fileInputs = multi.getFileNames();
    while (fileInputs.hasMoreElements()) {
      String fileInput = (String) fileInputs.nextElement();
      String fileName = multi.getFilesystemName(fileInput);
      String originalFileName = multi.getOriginalFileName(fileInput);
      if (fileName != null) {
        // 게시글 등록 실패한 경우 업로드 파일 삭제
        if (boardId == null) {
          File file = FileUtil.getUploadedFile(fileName);
          if (file.exists()) {
            file.delete();
          }
        } else {
          FileDto fileDto = new FileDto();
          fileDto.setBoardId(boardId);
          fileDto.setName(fileName);
          fileDto.setPath(FileUtil.FILE_PATH);
          fileDto.setOriginalName(originalFileName);
          FileDao fileDao = new FileDao();
          fileDao.save(fileDto);
        }
      }
    }

    response.sendRedirect("/board/boardList.jsp" + queryString);
    return;
  } // 게시글 등록 로직 종료

  // 게시글 등록 폼 조회
  // 게시글 검색 조건 (조건 유지하며 페이지 이동)
  String pageStr = StringUtil.nvl(request.getParameter("page"));
  String search = StringUtil.nvl(request.getParameter("search"));
  String categoryId = StringUtil.nvl(request.getParameter("categoryId"));
  String fromDate = StringUtil.nvl(request.getParameter("fromDate"));
  String toDate = StringUtil.nvl(request.getParameter("toDate"));
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                      <option value="${category.categoryId}">${category.name}</option>
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
                     value="${board == null ? "" : board.writer}"
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
                     value="${board == null ? "" : board.title}"
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
                        onchange="validContent()">${board == null ? "" : board.content}</textarea>
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
              <span class="file-error">${fileError}</span>
            </div>
          </div>

          <div class="button-container">
            <button type="button" class="button-cancel">
              <c:url value="boardList.jsp" var="boardList">
                <c:param name="page" value="<%=pageStr%>"/>
                <c:param name="search" value="<%=search%>"/>
                <c:param name="categoryId" value="<%=categoryId%>"/>
                <c:param name="fromDate" value="<%=fromDate%>"/>
                <c:param name="toDate" value="<%=toDate%>"/>
              </c:url>
              <a href="${boardList}" class="button-cancel-a">취소</a>
            </button>
            <button type="submit" class="button-save">저장</button>
          </div>

        </div>

        <%--검색조건--%>
        <input type="hidden" name="search" value="<c:out value="<%=search%>"/>">
        <input type="hidden" name="fromDate" value="<c:out value="<%=fromDate%>"/>">
        <input type="hidden" name="toDate" value="<c:out value="<%=toDate%>"/>">
        <input type="hidden" name="page" value="<c:out value="<%=pageStr%>"/>">

      </form>

    </body>
</html>

<%
  session.removeAttribute("board");
  session.removeAttribute("fileError");
%>

