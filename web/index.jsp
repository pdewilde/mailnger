<%@ page import="Sample.HelloWorld" %><%--
  Created by IntelliJ IDEA.
  User: Parker
  Date: 6/27/2017
  Time: 9:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Simple jsp page</title>
  </head>
  <body>
    <h3 class="message"><%=HelloWorld.getMessage()%></h3>
  </body>
</html>
