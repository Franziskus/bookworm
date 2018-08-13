package eu.heblich.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendFile
 * This Servlets send a file from the local file system to the client
 */
@WebServlet("/SendFile/*")
public class SendFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SendFile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String s = request.getParameter("path");
		if(s != null){
			File f = new File(s);
			FileInputStream fis = new FileInputStream(f);
			response.setContentType(Files.probeContentType(f.toPath()));
			response.setContentLengthLong(f.length());
			int avalable;
			byte[] arr = new byte[1024];
			while((avalable = fis.available()) > 0){
				if(avalable > arr.length){
					fis.read(arr);
					response.getOutputStream().write(arr);
					response.flushBuffer();
				}else{
					arr = new byte[avalable];
					fis.read(arr);
					response.getOutputStream().write(arr);
					response.flushBuffer();
				}
			}
			fis.close();
		}else{
			response.getOutputStream().print("Please add path.");
			response.getOutputStream().flush();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
