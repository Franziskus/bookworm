<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="eu.heblich.db.LocalDB"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored = "false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">

function allTables(){
	document.getElementById("sqlLine").value = "select * from sys.systables st LEFT OUTER join sys.sysschemas ss on (st.schemaid = ss.schemaid) where ss.schemaname ='APP'";
}
function selectFileElement(){
	document.getElementById("sqlLine").value = "select * from app.FileElement";
}
function selectWordElement(){
	document.getElementById("sqlLine").value = "select * from app.WordElement";
}
function selectWordFileConnector(){
	document.getElementById("sqlLine").value = "select * from app.WordFileConnector";
}

function selectWord(){
	document.getElementById("sqlLine").value = "select FileElement.id, FileElement.filename, WordElement.word from FileElement, WordFileConnector, WordElement WHERE FileElement.id = WordFileConnector.fileId AND WordElement.id = WordFileConnector.wordId AND WordElement.word like ";
}
function selectWordsOfFile(){
	document.getElementById("sqlLine").value = "select FileElement.id, FileElement.filename, WordElement.word from FileElement, WordFileConnector, WordElement WHERE FileElement.id = WordFileConnector.fileId AND WordElement.id = WordFileConnector.wordId AND FileElement.id = ";
}
</script>
</head>
<body>
<input type="button" value="all App tables" name="all App tables" onclick="allTables()"/>
<input type="button" value="Select FileElement" name="Select FileElement" onclick="selectFileElement()"/>
<input type="button" value="Select WordElement" name="Select WordElement" onclick="selectWordElement()"/>
<input type="button" value="Select WordFileConnector" name="Select WordFileConnector" onclick="selectWordFileConnector()"/>
<input type="button" value="Select Word" name="Select Word" onclick="selectWord()"/>
<input type="button" value="Select Words Of File" name="Select Words Of File" onclick="selectWordsOfFile()"/>
<br/>
<form action="" method="post">
    <input id="sqlLine" type="text" name="sqlLine" size="100" value="${param.sqlLine}" />
    <input type="submit" />
</form>
<%
	String s = request.getParameter("sqlLine");
	if(s != null){
		s = s.trim();
		//select
		//if(s.length() > 6 && s.substring(0,6).equalsIgnoreCase("select")){
			{
			Statement stmt = null;
			try{
				Connection con = LocalDB.INSTANCE.getConnection();
				stmt = con.createStatement();
				
				ResultSet rs = stmt.executeQuery(s);
				boolean first = true;
				ResultSetMetaData rsmd = null;
		        while (rs.next()) {
		        	if(first){
		        		rsmd = rs.getMetaData();
						%>
						<table>
						  <tr>
						<%
						for(int j = 1; j < rsmd.getColumnCount() + 1; j++){
							%><th><%=rsmd.getColumnName(j)%></th><%
						}
						%></tr><%
						first = false;
		        	}
		        	%><tr><%
		        	for(int j = 1; j < rsmd.getColumnCount() + 1; j++){
		        		%><td><%=rs.getObject(j).toString()%></td><%
		        	}
		        	%></tr><%
		        }
			}catch(SQLException e){
				String error = LocalDB.stringSQLException(e);
				%><pre><code><%=error%></code></pre><%
				System.err.println(error);
			}
			
			try{
				if(stmt != null)
					stmt.close();
			}catch(SQLException e){
				String error = LocalDB.stringSQLException(e);
				%><pre><code><%=error%></code></pre><%
				System.err.println(error);
			}
		}
		
		%><%
	}else{
		%>null<%
	}
%>
</body>
</html>