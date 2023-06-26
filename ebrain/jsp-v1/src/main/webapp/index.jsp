<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.study.util.ConnectionUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">HelloDDD</a>


<%
    Connection connection = ConnectionUtil.getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement("select 1");
    ResultSet resultSet = preparedStatement.executeQuery();
    String queryResult = "";
    while (resultSet.next()) {
        queryResult = resultSet.getString(1);
    }
    out.println(queryResult);

%>

</body>
</html>
