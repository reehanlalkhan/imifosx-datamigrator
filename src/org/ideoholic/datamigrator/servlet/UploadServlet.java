package org.ideoholic.datamigrator.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
					 // if Member data option is selected
						// MemberDataImporter mdi = new MemberDataImporter(fullFilePath);
						// mdi.importMemberData("00000020");
						LoanDataImporter ldi = new LoanDataImporter(fullFilePath);
						ldi.importLoanData();
					
					out.println("Uploaded Filename: " + fileName + "<br>");
					out.println("Uploaded Field Name: " + fieldName + "<br>");
					out.println("Uploaded Content Type: " + contentType + "<br>");
					out.println("Uploaded is in memory: " + isInMemory + "<br>");
					out.println("Uploaded size in bytes: " + sizeInBytes + "<br>");
					// fi.delete();
				}
			}
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
			out.println("<H1>");
			out.println("There was exception while uploading");
			out.println("Please check:");
			out.println("<ol>");
			out.println("<li>If the given input data is in the proper format</li>");
			out.println("<li>If the correct option is selected for the input data type</li>");
			out.println("<li>If the database is up and running</li>");
			out.println("</ol>");
			out.println("</H1>");
			out.println("Contact Ideoholic team in case the issue is not resolved");
			out.println("</body>");
			out.println("</html>");
			ex.printStackTrace();
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