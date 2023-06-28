<%@ page import="com.study.dao.BoardDao" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="com.study.dao.FileDao" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.study.dao.CategoryDao" %>
<%@ page import="com.study.dto.CategoryDto" %>
<%@ page import="com.study.validation.BoardValidationUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  if (request.getMethod().equalsIgnoreCase("post")) {
    request.setCharacterEncoding("utf-8");

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
        // 파일사이즈 초과
        session.setAttribute("fileError", "10MB를 넘을 수 없습니다.");
        response.sendRedirect("/board/register.jsp");
        return;
    }

    // 게시글 등록
    Long categoryId = Long.parseLong(multi.getParameter("categoryId"));
    String writer = multi.getParameter("writer");
    String password = multi.getParameter("password");
    String title = multi.getParameter("title");
    String content = multi.getParameter("content");

    BoardDto boardDto = new BoardDto();
    boardDto.setCategoryId(categoryId);
    boardDto.setTitle(title);
    boardDto.setWriter(writer);
    boardDto.setContent(content);
    boardDto.setPassword(password);

    // 게시글 유효성 검증
    if (!BoardValidationUtil.validBoard(boardDto)) {
      session.setAttribute("board", boardDto);
      response.sendRedirect("/board/register.jsp");
      return;
    }

    BoardDao boardDao = new BoardDao();
    long boardId = boardDao.register(boardDto);

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
  }
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>글 작성</title>
    <link rel="stylesheet" href="/resources/css/board/register.css">
    <script src="/resources/js/board/register.js"></script>
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
              <select name="categoryId" class="category-select">

                <%
                  CategoryDao categoryDao = new CategoryDao();
                  Set<Map.Entry<String, List<CategoryDto>>> categories = categoryDao.findAll().entrySet();
                %>

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
            <div class="file-input-container">
              <div class="file-input-container-first">
                <input type="text"
                       class="file-input-first"
                       value="${fileError == null ? "" : fileError}"
                       style="${fileError == null ? "color : black" : "color : red"}"
                       disabled>
                <label for="file-input-first">파일 찾기</label>
                <input type="file"
                       name="file1"
                       id="file-input-first"
                       onchange="uploadFile(this)">
              </div>
              <div class="file-input-container-second">
                <input type="text" value="" class="file-disabled-first" disabled>
                <label for="file-input-second">파일 찾기</label>
                <input type="file"
                       name="file2"
                       id="file-input-second"
                       class="file-input-second"
                       onchange="uploadFile(this)">
              </div>
              <div class="file-input-container-third">
                <input type="text" value="" disabled>
                <label for="file-input-third">파일 찾기</label>
                <input type="file"
                       name="file3"
                       id="file-input-third"
                       class="file-input-third"
                       onchange="uploadFile(this)">
              </div>
            </div>
          </div>

          <div class="button-container">
            <button class="button-cancel">취소</button>
            <button type="submit" class="button-save">저장</button>
          </div>
        </div>

      </form>

    </body>
</html>

<%
  session.removeAttribute("board");
  session.removeAttribute("fileError");
%>