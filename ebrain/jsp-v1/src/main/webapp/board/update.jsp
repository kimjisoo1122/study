<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.study.util.StringUtil" %>
<%@ page import="com.study.dao.BoardDao" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="com.study.dao.FileDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.study.validation.BoardValidationUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%
  if (request.getMethod().equalsIgnoreCase("post")) {
    request.setCharacterEncoding("UTF-8");

    // multipart 설정
    String encType = "UTF-8";
    String filePath = "C:\\Users\\kimji\\IdeaProjects\\study\\ebrain\\jsp-v1\\src\\main\\webapp\\WEB-INF\\upload";
    File file = new File(filePath);
    if (!file.exists()) {
      file.mkdirs();
    }

    int fileMaxSize = 1024 * 1024 * 10;
    MultipartRequest multi = null;
    try {
      multi = new MultipartRequest(request, filePath, fileMaxSize, encType,
              new DefaultFileRenamePolicy());
    } catch (IOException e) {
      e.printStackTrace();
      // 파일사이즈 초과
      session.setAttribute("fileError", "10MB를 넘을 수 없습니다.");
      response.sendRedirect("/board/update.jsp");
      return;
    }

    // 업데이트 정보 파싱
    String writer = multi.getParameter("writer");
    String title = multi.getParameter("title");
    String content = multi.getParameter("content");
    String password = multi.getParameter("password");
    Long boardId = Long.parseLong(multi.getParameter("boardId"));

    // 검색조건 파싱
    String fromDate = multi.getParameter("fromDate");
    String toDate = multi.getParameter("toDate");
    String categoryId = multi.getParameter("categoryId");
    String search = multi.getParameter("search");
    String pageStr = multi.getParameter("page");

    // 원글 조회
    BoardDao boardDao = new BoardDao();
    BoardDto findBoard = boardDao.findById(boardId);
    findBoard.setWriter(writer);
    findBoard.setTitle(title);
    findBoard.setContent(content);

    // 게시글 유효성 검증
    String queryString = "?boardId=" + boardId + "&search=" + URLEncoder.encode(search)
            + "&categoryId=" + categoryId + "&fromDate=" + fromDate + "&toDate=" + toDate + "&page=" + pageStr;
    if (!password.equals(findBoard.getPassword())
            || !BoardValidationUtil.validBoard(findBoard) ) {
      session.setAttribute("board", findBoard);
      response.sendRedirect("/board/update.jsp" + queryString);
      return;
    }

    if (password.equals(findBoard.getPassword())) {

    }

    // 게시글 업데이트
    Map<String, String> updateMap = new HashMap<>();
    updateMap.put("writer", writer);
    updateMap.put("title", title);
    updateMap.put("content", content);
    boardDao.update(updateMap, boardId);

    // 파일 db 저장
    Enumeration fileInputs = multi.getFileNames();
    while (fileInputs.hasMoreElements()) {
      String fileInput = (String) fileInputs.nextElement();
      String fileName = multi.getFilesystemName(fileInput);
      String originalFileName = multi.getOriginalFileName(fileInput);
      if (fileName != null) {
        FileDto fileDto = new FileDto();
        fileDto.setBoardId(boardId);
        fileDto.setName(fileName);
        fileDto.setPath(filePath);
        fileDto.setOriginalName(originalFileName);
        FileDao fileDao = new FileDao();
        fileDao.save(fileDto);
      }
    }

    // 파일 삭제 확인
    String[] fileIds = multi.getParameterValues("fileId");
    if (fileIds != null) {
      FileDao fileDao = new FileDao();
      for (String fileIdStr : fileIds) {
        Long fileId = Long.parseLong(fileIdStr);
        FileDto fileDto = fileDao.findById(fileId);

        // 실제 파일 삭제
        File regisetdFile = new File(fileDto.getPath() + File.separator + fileDto.getName());
        if (regisetdFile.exists()) {
          if (regisetdFile.delete()) {
            // 파일 db 삭제
            fileDao.delete(fileId);
          }
        }
      }
    }
    response.sendRedirect("/board/boardList.jsp" + queryString);
    return;
  }

  // 게시글 조회
  BoardDto board = (BoardDto) session.getAttribute("board");
  if (board == null) {
    Long boardId = Long.parseLong(request.getParameter("boardId"));
    BoardDao boardDao = new BoardDao();
    board = boardDao.findById(boardId);
  }

  // 첨부파일 조회
  FileDao fileDao = new FileDao();
  List<FileDto> files = fileDao.findByBoardId(board.getBoardId());

  // 게시글 검색 조건 (조건 유지하며 페이지 이동)
  String pageStr = StringUtil.nvl(request.getParameter("page"));
  String search = StringUtil.nvl(request.getParameter("search"));
  String categoryId = StringUtil.nvl(request.getParameter("categoryId"));
  String fromDate = StringUtil.nvl(request.getParameter("fromDate"));
  String toDate = StringUtil.nvl(request.getParameter("toDate"));
%>

<html>

  <head>
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
                  <div class="file-name">${file.name}</div>
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
                         value="${fileError == null ? "" : fileError}"
                         style="${fileError == null ? "color : black" : "color : red"}"
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
              <c:url value="board.jsp" var="board">
                <c:param name="page" value="<%=pageStr%>"/>
                <c:param name="search" value="<%=search%>"/>
                <c:param name="categoryId" value="<%=categoryId%>"/>
                <c:param name="fromDate" value="<%=fromDate%>"/>
                <c:param name="toDate" value="<%=toDate%>"/>
                <c:param name="boardId" value="<%=String.valueOf(board.getBoardId())%>"/>
              </c:url>
              <a href="${board}" class="button-cancel-a">취소</a>
            </button>
            <button type="submit" class="button-save">저장</button>
          </div>

      </div>

      <%--검색조건 유지--%>
      <input type="hidden" name="boardId" value="<c:out value="<%=board.getBoardId()%>"/>">
      <input type="hidden" name="search" value="<c:out value="<%=search%>"/>">
      <input type="hidden" name="fromDate" value="<c:out value="<%=fromDate%>"/>">
      <input type="hidden" name="toDate" value="<c:out value="<%=toDate%>"/>">
      <input type="hidden" name="categoryId" value="<c:out value="<%=categoryId%>"/>">
      <input type="hidden" name="page" value="<c:out value="<%=pageStr%>"/>">

    </form>

  </body>

</html>

<%
  session.removeAttribute("board");
  session.removeAttribute("fileError");
%>