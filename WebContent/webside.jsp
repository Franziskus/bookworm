<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="eu.heblich.db.LocalDB"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="websocket.js"></script>
        
        <style type="text/css">
        	
body {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 80%;
    background-color: #1f1f1f;
}

#wrapper {
    width: 100%;
    margin: auto;
    text-align: left;
    color: #d9d9d9;
}

p {
    text-align: left;
}

.button {
    display: inline;
    color: #fff;
    background-color: #f2791d;
    padding: 8px;
    margin: auto;
    border-radius: 8px;
    -moz-border-radius: 8px;
    -webkit-border-radius: 8px;
    box-shadow: none;
    border: none;
}

.button:hover {
    background-color: #ffb15e;
}
.button a, a:visited, a:hover, a:active {
    color: #fff;
    text-decoration: none;
}

#addDevice {
    text-align: center;
    width: 960px;
    margin: auto;
    margin-bottom: 10px;
}

#addDeviceForm {
    text-align: left;
    width: 400px;
    margin: auto;
    padding: 10px;
}

#addDeviceForm span {
    display: block;
}

#content {
	display: inline-block;
    margin: auto;
    width: 340px;
}

.device {
    width: 300px;
    height: 150px;
    margin: 10px;
    padding: 16px;
    color: #fff;
    vertical-align: top;
    border-radius: 8px;
    -moz-border-radius: 8px;
    -webkit-border-radius: 8px;
    display: inline-block;
}

.device.off {
    background-color: #c8cccf;
}

.device span {
    display: block;
}

.deviceName {
    text-align: center;
    font-weight: bold;
    margin-bottom: 12px;
}

.removeDevice {
    margin-top: 12px;
    text-align: center;
}

.device.DONE {
    background-color: #5eb85e;
}

.device.DONE a:hover {
    color: #a1ed82;
}

.device.PROCESSING {   
    background-color: #0f90d1;
}

.device.PROCESSING a:hover {
    color: #4badd1;
}

.device.FOUND {
    background-color: #c2a00c;
}

.device.FOUND a:hover {
    color: #fad232;
}

.device.NONE {
    background-color: #db524d;
}

.device.NONE a:hover {
    color: #ff907d;
}

.device a {
    text-decoration: none;
}

a {
    color: #82b1ff;
}

a:visited, a:active, a:hover {
    color: #b2cfff;
}

.device a:visited, a:active, a:hover {
    color: #fff;
}

.device a:hover {
    text-decoration: underline;
}

#look {
	display: inline-block;
	width: calc(100% - 350px);
	vertical-align:top;
    
}
        </style>
    </head>
    <body>

        <div id="wrapper">
          <div id="look">
            <form action="" method="post">
			    <input id="sqlLine" type="text" name="sqlLine" size="50" value="${param.sqlLine}" />
			    <input type="submit" />
			</form>
<%
	String s = request.getParameter("sqlLine");
	if(s != null){
		 
		s = s.trim();
		//String[] words = s.split(" ");
		String line = "select FileElement.id, FileElement.filename, WordElement.word from FileElement, WordFileConnector, WordElement WHERE FileElement.id = WordFileConnector.fileId AND WordElement.id = WordFileConnector.wordId AND WordElement.word like ";
		line = line.concat("'");
		line = line.concat(s);
		line = line.concat("'");
		//select
		//if(s.length() > 6 && s.substring(0,6).equalsIgnoreCase("select")){
			{
			Statement stmt = null;
			try{
				Connection con = LocalDB.INSTANCE.getConnection();
				stmt = con.createStatement();
				
				ResultSet rs = stmt.executeQuery(line);
				boolean first = true;
				ResultSetMetaData rsmd = null;
		        while (rs.next()) {
		        	if(first){
		        		rsmd = rs.getMetaData();
						%>
						<table>
						  <tr><%
						for(int j = 1; j < rsmd.getColumnCount() + 1; j++){
							%><th><%=rsmd.getColumnName(j)%></th><%
						}
						%></tr><%
						first = false;
		        	}
		        	%><tr><%
		        	for(int j = 1; j < rsmd.getColumnCount() + 1; j++){
		        		if(j == 2){
		        			String fullPath = rs.getObject(j).toString();
		        			String fileName = fullPath;
		        			int lastIndex = fileName.lastIndexOf("/");
		        			if(lastIndex == -1){
		        				lastIndex = fileName.lastIndexOf("\\");
		        			}
		        			lastIndex = lastIndex+1;
		        			fileName = fileName.substring(lastIndex);
		        		%><td><a href="<%="SendFile/".concat(fileName).concat("?path=").concat(rs.getObject(j).toString())%>"><%=fullPath%></a></td><%
		        		}else{
		        	    	%><td><%=rs.getObject(j).toString()%></td><%
		        		}
		        	}
		        	%></tr><%
		        }
			}catch(SQLException e){
				String error = LocalDB.stringSQLException(e);
				%><pre><code><%=error%></code></pre><%
				System.err.println(error);
			}
			%></table><%
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
		%><!-- empty --><%
	}
%>
        </div>
         <div id="content"/>  
        </div>

    </body>
</html>