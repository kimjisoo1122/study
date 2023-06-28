<%@ page import="java.io.File" %>
<%@ page import="com.study.dao.FileDao" %>
<%@ page import="com.study.dto.FileDto" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileNotFoundException" %>
<%@ page import="java.io.OutputStream" %>
<%@ page contentType="text/html;charset=UTF-8" %>


<%
  request.setCharacterEncoding("UTF-8");
  // 파일dto 조회
  Long fileId = Long.parseLong(request.getParameter("fileId"));
  FileDao fileDao = new FileDao();
  FileDto fileDto = fileDao.findById(fileId);

  // 파일 생성
  File file = new File(fileDto.getPath() + File.separator + fileDto.getName());

  // 한글 오리지널파일명 인코딩
  String fileName = new String(fileDto.getOriginalName().getBytes("utf-8"), "8859_1");

  try (FileInputStream is = new FileInputStream(file)) {
    // 지정되지 않은 파일 형식
    response.setContentType("application/octet-stream");
    // 다운로드 화면 출력
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

    try (OutputStream os = response.getOutputStream()) {
      int length;
      byte[] buffer = new byte[(int)file.length()];
      while ((length = is.read(buffer)) > 0) {
        os.write(buffer, 0 ,length);
      }
    }

  } catch (FileNotFoundException e) {
    e.printStackTrace();
  }
%>

<html>
<head>
  <title>파일 다운로드</title>
</head>
<body>

</body>
</html>
