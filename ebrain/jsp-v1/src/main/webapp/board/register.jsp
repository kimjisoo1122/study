<%@ page import="com.study.dao.BoardDao" %>
<%@ page import="com.study.dto.BoardDto" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  if (request.getMethod().equalsIgnoreCase("post")) {
    request.setCharacterEncoding("utf-8");
    String contentType = request.getContentType();
    System.out.println("contentType = " + contentType);

    String filePath = "C:\\upload";
    int maxSize = 1024 * 1024 * 10; // 10MB
    MultipartRequest multi = new MultipartRequest(request, filePath, maxSize, "UTF-8", new DefaultFileRenamePolicy());

    Long categoryId = Long.parseLong(multi.getParameter("categoryId"));
    String title = multi.getParameter("title");
    String writer = multi.getParameter("writer");
    String content = multi.getParameter("content");
    String password = multi.getParameter("password");
    BoardDto boardDto = new BoardDto(categoryId, title, writer, content, password);

    BoardDao boardDao = new BoardDao();
    long boardId = boardDao.register(boardDto);

    String file1 = multi.getFilesystemName("file1");
    String file2 = multi.getFilesystemName("file2");
    String file3 = multi.getFilesystemName("file3");
    if (file1 != null) {

    }
  }
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>글 작성</title>
    <link rel="stylesheet" href="/resources/css/board/register.css" ></link>
    <script src="/resources/js/board/register.js"></script>
  </head>

    <body>

      <form method="post" enctype="multipart/form-data" accept-charset="UTF-8">

        <div class="register-container">

          <div class="category-container">
            <div class="category-title">카테고리
              <span class="required-star">*</span>
            </div>
            <div class="category-select-container">
              <select name="categoryId" class="category-select">
                <optgroup label="Back-end">
                  <option value="1">JAVA </option>
                  <option value="3">Database</option>
                </optgroup>
              </select>
            </div>
          </div>

          <div class="writer-container">
            <div class="writer-title">작성자
              <span class="required-star">*</span>
            </div>
            <div class="writer-input-container">
              <input type="text" name="writer" class="writer-input">
            </div>
          </div>

          <div class="password-container">
            <div class="password-title">비밀번호
              <span class="required-star">*</span>
            </div>
            <div class="password-input-container">
              <input type="password" name="password" class="password-input">
              <input type="password" name="confirmPassword" class="password-input-confirm">
            </div>
          </div>

          <div class="title-container">
            <div class="title-title">제목
              <span class="required-star">*</span>
            </div>
            <div class="title-input-container">
              <input type="text" name="title" class="title-input">
            </div>
          </div>

          <div class="content-container">
            <div class="content-title">내용
              <span class="required-star">*</span>
            </div>
            <textarea name="content"  class="content-text"></textarea>
          </div>

          <div class="file-container">
            <div class="file-title">파일 첨부</div>
            <div class="file-input-container">
              <div class="file-input-container-first">
                <input type="text" value="" disabled>
                <label for="file-input-first">파일 찾기</label>
                <input type="file" name="file1" id="file-input-first" class="file-input-first">
              </div>
              <div class="file-input-container-second">
                <input type="text" value="" disabled>
                <label for="file-input-second">파일 찾기</label>
                <input type="file" name="file2" id="file-input-second" class="file-input-second">
              </div>
              <div class="file-input-container-third">
                <input type="text" value="" disabled>
                <label for="file-input-third">파일 찾기</label>
                <input type="file" name="file3" id="file-input-third" class="file-input-third">
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
