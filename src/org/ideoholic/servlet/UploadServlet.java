package org.ideoholic.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.fname_lname.test.MemberNameUtil;
import org.fname_lname.test.Pair;

public class UploadServlet extends HttpServlet {

	public UploadServlet() {
		// TODO Auto-generated constructor stub
	}

	private String filePath;
	private int maxFileSize = 50 * 1024;
	private int maxMemSize = 4 * 1024;

	public void init() {
		// Get the file location where it would be stored.
		filePath = getServletContext().getInitParameter("file-upload");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
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
		factory.setRepository(new File("/home/springboard/eclipse-workspace/imifosx-datamigrator"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(new ServletRequestContext(request));

			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");

			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
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
						System.out.println("Full File Path:" + fullFilePath);
						file = new File(fullFilePath);
					} else {
						fullFilePath = filePath + fileName.substring(fileName.lastIndexOf("\\") + 1);
						System.out.println("Full File Path:" + fullFilePath);
						file = new File(fullFilePath);
					}
					fi.write(file);
					writeToFile(file, fi.getInputStream());
					out.println("Uploaded Filename: " + fileName + "<br>");
					out.println("Uploaded Field Name: " + fieldName + "<br>");
					out.println("Uploaded Content Type: " + contentType + "<br>");
					out.println("Uploaded is in memory: " + isInMemory + "<br>");
					out.println("Uploaded size in bytes: " + sizeInBytes + "<br>");
//Start Of Creation Code					
					parse(fullFilePath);
					
					Pair<String, String> name;

//End of Creation Code			
					
				}
			}
			out.println("<form action='UploadServlet'>");
			out.println("<button>Upload Another File</button>");
			out.println("</form>");
			
			out.println("</body>");
			out.println("</html>");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	//Client Creation
	public void parse(String filepath) {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mifostenant-default", "root",
				"mysql");
		con.setAutoCommit(false);
		PreparedStatement pstm = null;
		FileInputStream input = new FileInputStream(filepath);
		
		HSSFWorkbook wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Row row;
	
		for (int i1 = 1; i1 <= sheet.getLastRowNum(); i1++) {
			row = sheet.getRow(i1);
			String display_name = row.getCell(2).getStringCellValue();
			String gender = row.getCell(4).getStringCellValue();
			String dob = row.getCell(5).getStringCellValue();
			String pan_no = row.getCell(7).getStringCellValue();
			String street = row.getCell(8).getStringCellValue();
			BigInteger office_id = BigInteger.valueOf(1);
			name = MemberNameUtil.getFistNameAndLastName(display_name);
			String firstname = name.first;
			String lastname = name.second;
			String var_ContactLicenceNumber = "MALE";
			int gender_cv_id;
			if (var_ContactLicenceNumber.equalsIgnoreCase(gender)) {
				gender_cv_id = 20;
			} else {
				gender_cv_id = 22;
			}
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			String activation_date = dtf.format(localDate);
			SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date_of_birth = null;
			try {

				String inputString = dob;
				date_of_birth = myFormat.format(fromUser.parse(inputString));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			String account_no_string = "00000020";
			int y = Integer.parseInt(account_no_string) + 1;
			String account_no = String.format("%09d", y);
			String sql = "INSERT INTO m_client(office_id,account_no,activation_date,firstname,"
					+ "lastname,display_name,gender_cv_id,date_of_birth) VALUES('" + office_id + "'," + "'"
					+ account_no + "','" + activation_date + "','" + firstname + "','" + lastname + "','"
					+ display_name + "','" + gender_cv_id + "','" + date_of_birth + "')";
			BigInteger product_id = BigInteger.valueOf(1);
			String submittedon_date = dtf.format(localDate);
			String approvedon_date = dtf.format(localDate);
			String activatedon_date = dtf.format(localDate);
			String currency_code = "INR";
			short currency_digits = 2;
			double nm = 0;
			String nominal_annual_interest_rate = String.format("%.4f", nm);
			short interest_compounding_period_enum = 1;
			short interest_calculation_type_enum = 1;
			short interest_calculation_days_in_year_type_enum = 365;
			pstm = (PreparedStatement) con.prepareStatement(sql);
			pstm.execute();
			String id_old = null;
			PreparedStatement statement = con
					.prepareStatement("select id from m_client where account_no=" + "'" + account_no + "'");
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				id_old = result.getString(1);
			} else

			{
				System.out.println("No id for the client");
			}
			int foo = Integer.parseInt(id_old);
			BigInteger client_id = BigInteger.valueOf(foo);
			
			String sql1 = "INSERT into m_savings_account(account_no,client_id,product_id,submittedon_date,approvedon_date,activatedon_date,currency_code,currency_digits,nominal_annual_interest_rate,\n"
					+ "interest_compounding_period_enum,interest_calculation_type_enum,interest_calculation_days_in_year_type_enum) VALUES('"
					+ account_no + "','"+client_id+"'," + "'" + product_id + "','" + submittedon_date + "','"+approvedon_date+"','"+activatedon_date+"','" + currency_code
					+ "','" + currency_digits + "','" + nominal_annual_interest_rate + "','"
					+ interest_compounding_period_enum + "','" + interest_calculation_type_enum + "','"
					+ interest_calculation_days_in_year_type_enum + "')";


			BigInteger address_id = BigInteger.valueOf(1);
			BigInteger address_type_id = BigInteger.valueOf(21);

			byte is_active = 1;

			String sql2 = "INSERT INTO m_client_address(client_id,address_id,\n"
					+ "address_type_id,is_active) VALUES('" + client_id + "'," + "'" + address_id + "','"
					+ address_type_id + "','" + is_active + "')";
			int document_type_id = 23;
			String document_key = pan_no;
			int status = 200;
			int active = 200;

			String sql3 = "INSERT INTO  m_address(id,street) VALUES('" + client_id + "'," + "'" + street + "')";
			String sql4 = "INSERT INTO m_client_identifier(client_id,document_type_id,\n"
					+ "document_key,status,active) VALUES('" + client_id + "'," + "'" + document_type_id + "','"
					+ document_key + "','" + status + "','" + active + "')";
	      		pstm = (PreparedStatement) con.prepareStatement(sql1); pstm.execute(); 
			    pstm = (PreparedStatement) con.prepareStatement(sql2); pstm.execute();
			    pstm = (PreparedStatement) con.prepareStatement(sql3); pstm.execute(); 
			    pstm =  (PreparedStatement) con.prepareStatement(sql4); pstm.execute();

		}
		con.commit();
		pstm.close();
		con.close();
		input.close();
		System.out.println("Success import excel to mysql table");
	} catch (ClassNotFoundException e) {
		System.out.println(e);
	} catch (SQLException ex) {
		System.out.println(ex);
	} catch (IOException ioe) {
		System.out.println(ioe);
	}
}
	// Client creation code ends here
	

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

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servlet upload</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("  <h3>File Upload:</h3>");
		out.println("  Select a file to upload: <br />");
		out.println("  <form action = 'UploadServlet' method = 'post' enctype = 'multipart/form-data'>");
		out.println("     <input type = 'file' name = 'file' size = '50' />");
		out.println("     <br />");
		out.println("     <input type = 'submit' value = 'Upload File' />");
		out.println("  </form>");
		out.println("</body>");
		out.println("</html>");
	}
}
