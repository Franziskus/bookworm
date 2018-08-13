<%@page import="java.io.File"%>
<html>
<body>
<h2>Hello World!</h2>


<table>
<%
String pathToJspInWebapp = request.getSession().getServletContext().getRealPath(request.getServletPath());
File jsp = new File(pathToJspInWebapp);  //eg. /WEB-INF/jsp/my.jsp
File directory = jsp.getParentFile();
File[] list = directory.listFiles();
for(int i = 0; i < list.length; i++){
	String s = list[i].getName();
	if(list[i].isFile()){
%>
	<tr>
		<td>
			<%=i %>.
		</td>
		<td>
			<a href="<%=s%>"><%=s%></a>
		</td>
	</tr>
<%	}
}%>
</table>
</body>
</html>
