package org.ideoholic.datamigrator.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadDisplayServlet extends HttpServlet {

	private static final long serialVersionUID = 2233093329064205559L;
	
	public static void printUploadPage(PrintWriter out) {
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servlet upload</title>");
		out.println("<script src='js/customfunctions.js'></script>");
		out.println("</head>");
		out.println("<body>");
		out.println("  <h3>File Upload:</h3>");
		out.println("  Select a file to upload: <br />");
		out.println(
				"  <form id='fileUploadForm' action = 'UploadServlet' method = 'POST' enctype = 'multipart/form-data'>");
		out.println("<input type = 'radio' name = \"f1\" id =\"11\" value=\"Member_File\">Member Creation<br> "); 
		out.println( "<input type = \"radio\" name = \"f1\" id =\"12\" value=\"Loan_File\"> Loan Creation<br>");
		out.println( "<input type = \"text\" name = \"t1\" id =\"101\" > <br>"); 
		out.println( "<input type = \"radio\" name = \"f1\" id =\"13\" value=\"Loan_Transaction_File\"> Loan Transaction<br>");
		out.println( "<input type = \"text\" name = \"tt1\" id =\"103\" > <br>");
		out.println( "<input type = \"radio\" name = \"f1\" id =\"14\" value=\"Savings_File\"> Savings Account<br>");
		out.println( "<input type = \"text\" name = \"t2\" id =\"102\" > <br>");
		out.println( "<input type = \"radio\" name = \"f1\" id =\"15\" value=\"Savings_Transaction_File\"> Savings Transaction<br>");
		out.println( "<input type = \"text\" name = \"tt2\" id =\"104\" > <br>");
		out.println( "<input type = \"radio\" name = \"f1\" id =\"16\" value=\"Share_Account\"> Share Account<br>");
		out.println( "<input type = \"text\" placeholder ='YYYY-MM-DD' name = \"tt3\" id =\"105\" > <br>");
		out.println("     <input type = 'file' name = 'file' size = '50' />");
		out.println("     <br />");
		out.println("     <input type = 'submit' value = 'Upload File' />");
		out.println("  </form>");
		out.println("  <div style='background-color:#f1f1f1'>");
		out.println("    <span><a href='#' onclick='logout()'>Logout</a></span>");
		out.println("  </div>");
		out.println("</body>");
		out.println("</html>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Display Printuploadpage");
		String value = request.getParameter("f1");
		String value1 = request.getParameter("f1");
		System.out.println("MEMBER FILE"+value);
		System.out.println("LOAN FILE"+value1);
		PrintWriter out = response.getWriter();
		printUploadPage(out);
		out.close();
	}

}
