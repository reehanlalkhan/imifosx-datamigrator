package org.ideoholic.datamigrator.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.ideoholic.datamigrator.excelservice.LoanDataImporter;
import org.ideoholic.datamigrator.excelservice.LoanTransactionImporter;
import org.ideoholic.datamigrator.excelservice.MemberDataImporter;
import org.ideoholic.datamigrator.excelservice.SavingsAccountImporter;
import org.ideoholic.datamigrator.excelservice.SavingsDataImporter;
import org.ideoholic.datamigrator.utils.DBUtils;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 2233093329064205559L;

	private String filePath;
	// Allowing 50MB of data per file
	private int maxFileSize = 50 * 1000 * 1024;
	private int maxMemSize = 4 * 1024;

	public void init() {
		// Get the file location where it would be stored.
		filePath = getServletContext().getInitParameter("file-upload");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fullFilePath = null;
		boolean isMultipart;
		File file;
		String userSelectedOption = null;

		String userInputedOption = null;
		String inputValue=null;
		String inputDate=null;
	
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		if (!isMultipart) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<p>No file uploaded</p>");
			out.println("</body>");
			out.println("</html>");
			return;
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();

		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);

		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(filePath));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(request));

			// Process the uploaded file items
			Iterator<FileItem> i = fileItems.iterator();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");

			while (i.hasNext()) {
				FileItem fi = i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();

					// Write the file
					if (fileName.lastIndexOf("\\") >= 0) {
						fullFilePath = filePath + fileName.substring(fileName.lastIndexOf("\\"));
						System.out.println("If Full File Path:" + fullFilePath);
						file = new File(fullFilePath);
					} else {
						fullFilePath = filePath + fileName.substring(fileName.lastIndexOf("\\") + 1);
						System.out.println("Else Full File Path:" + fullFilePath);
						file = new File(fullFilePath);
					}
					// fi.write(file);
					writeToFile(file, fi.getInputStream());

					out.println("Uploaded Filename: " + fileName + "<br>");
					out.println("Uploaded Field Name: " + fieldName + "<br>");
					out.println("Uploaded Content Type: " + contentType + "<br>");
					out.println("Uploaded is in memory: " + isInMemory + "<br>");
					out.println("Uploaded size in bytes: " + sizeInBytes + "<br>");
				} else {
					//userSelectedOption = fi.getString();
					userInputedOption = fi.getFieldName();
					if("f1".equalsIgnoreCase(fi.getFieldName())) {
						userSelectedOption = fi.getString();
					}
					
					if ("t1".equalsIgnoreCase(fi.getFieldName()))
					{
						if(!"".equalsIgnoreCase(fi.getString()))
							
						{
							inputValue=fi.getString();	
						}
						
						System.out.println("VALUE "+inputValue);
					}
					else if ("t2".equalsIgnoreCase(fi.getFieldName()))
					{
						if(!"".equalsIgnoreCase(fi.getString()))
							{
							inputValue=fi.getString();
							}
						System.out.println("VALUE "+inputValue);
					}
					else if ("tt1".equalsIgnoreCase(fi.getFieldName()))
					{
						if(!"".equalsIgnoreCase(fi.getString()))
							{
							inputValue=fi.getString();
							}
						System.out.println("VALUE "+inputValue);
					}
					else if ("tt2".equalsIgnoreCase(fi.getFieldName()))
					{
						if(!"".equalsIgnoreCase(fi.getString()))
							{
							inputValue=fi.getString();
							}
						System.out.println("VALUE "+inputValue);
						System.out.println("NO INPUT VALUE IN VARIABLE INPUT tt2");
					}
					else if ("tt3".equalsIgnoreCase(fi.getFieldName()))
					{
						
							
						inputDate=fi.getString();
							
						System.out.println("INPUT DATE "+inputDate);
					}
					System.out.println("ALL USER INPUTED OPTIONS = : "+userSelectedOption);
				}
			}
			runServiceBasedOnUserSelection(userSelectedOption,fullFilePath,inputValue,inputDate);
			out.println("<form action='UploadServlet'>");
			out.println("<button>Upload Another File</button>");
			out.println("</form>");

			out.println("</body>");
			out.println("</html>");
		} catch (Exception ex) {
			System.out.println(ex);
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<H3>");
			out.println("There was exception while uploading");
			out.println("Please check:");
			out.println("<ol>");
			out.println("<li>If the given input data is in the proper format</li>");
			out.println("<li>If the correct option is selected for the input data type</li>");
			out.println("<li>If the database is up and running</li>");
			out.println("</ol>");
			out.println("</H3");
			out.println("Contact Ideoholic team in case the issue is not resolved");
			out.println("<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>");
			out.println("<font size='2'>");
			out.println("Exception:" + ex.getMessage());
			StackTraceElement[] stackTrace = ex.getStackTrace();
			for (StackTraceElement element : stackTrace) {
				out.println(element.toString());
			}
			out.println("</font>");
			out.println("</body>");
			out.println("</html>");
			ex.printStackTrace();
			// Rollback DB connection
			try {
				DBUtils.getInstance().rollbackTransaction();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				out.println("Rollback failed");
			}
			ex.printStackTrace();
		}
	}

	private void runServiceBasedOnUserSelection(String userSelectedOption, String fullFilePath, String inputValue, String inputDate)
			throws IOException, ClassNotFoundException, ParseException, SQLException {
		if ("Member_File".equals(userSelectedOption)) {
			// Official selected
			System.out.println("in member file" + userSelectedOption);
			MemberDataImporter mdi = new MemberDataImporter(fullFilePath);
			mdi.importMemberData("00000000");
		} else if ("Loan_File".equals(userSelectedOption)) {
			// all selected
			System.out.println("in loan file" + userSelectedOption);
			LoanDataImporter ldi = new LoanDataImporter(fullFilePath);
			ldi.importLoanData(inputValue);
		} else if ("Loan_Transaction_File".equals(userSelectedOption)) {
			// all selected
			LoanTransactionImporter ldi = new LoanTransactionImporter(fullFilePath);
			ldi.importTransactionData(inputValue);
		} else if ("Savings_Transaction_File".equals(userSelectedOption)) {
			// all selected
			SavingsDataImporter ldi = new SavingsDataImporter(fullFilePath);
			ldi.importSavingsData(inputValue,inputDate);
		} else if ("Savings_File".equals(userSelectedOption)) {
			// all selected
			SavingsAccountImporter ldi = new SavingsAccountImporter(fullFilePath);
			ldi.importSavingsAccount(inputValue);
		}
	}

	private void writeToFile(File f, InputStream inputStream) throws IOException {
		OutputStream outputStream = null;
		outputStream = new FileOutputStream(f);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			System.out.println("Buffer:" + buffer.toString());
			outputStream.write(buffer, 0, length);
		}
		// Closing the input/output file streams
		inputStream.close();
		outputStream.close();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		UploadDisplayServlet.printUploadPage(out);
		out.close();
	}
}
