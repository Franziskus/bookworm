<%@page import="eu.heblich.tika.Tika2"%>
<%@page import="eu.heblich.tika.MyFirstTika"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<pre><code>
<% 
Tika2 t2 = new Tika2();
%>
---- start ---
<%=t2.test("D:\\Files\\ddd.jpg")%>
---- end ---
</code></pre>
</body>
</html>