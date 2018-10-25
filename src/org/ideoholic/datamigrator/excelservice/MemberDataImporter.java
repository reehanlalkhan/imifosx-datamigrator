package org.ideoholic.datamigrator.excelservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;

import org.ideoholic.datamigrator.utils.Constants;
import org.ideoholic.datamigrator.utils.DBUtils;
import org.ideoholic.datamigrator.utils.DateUtils;
import org.ideoholic.datamigrator.utils.ExcelReaderUtils;
import org.ideoholic.datamigrator.utils.MemberNameUtil;
import org.ideoholic.datamigrator.utils.Pair;

public class MemberDataImporter implements Constants {
	private final ExcelReaderUtils excelReader;

	public MemberDataImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}

	public void importMemberData(String account_no_string) throws ParseException, ClassNotFoundException, SQLException {
		// This line will set account number start string to 0 if no number is passed
		account_no_string = account_no_string == null ? "00000000" : account_no_string;
		Iterator<MemberDataRow> excelIterator = excelReader.getWorkBookIterator(0);
		while (excelIterator.hasNext()) {
			MemberDataRow currentRow = excelIterator.next();
			String displayName = currentRow.getDisplayName();
			String gender = currentRow.getGender();
			String dob = currentRow.getDob();
			String panNo = currentRow.getPanNo();
			String street = currentRow.getStreet();
			Pair<String, String> name = MemberNameUtil.getFistNameAndLastName(displayName);
			String firstname = name.first;
			String lastname = name.second;
			int gender_cv_id=0;
			if (GENDER_MALE.equalsIgnoreCase(gender)) {
				gender_cv_id = MALE_CV_ID;
			} else {
				gender_cv_id = FEMALE_CV_ID;
			}

			String dateOfBirth = DateUtils.convertDateStringToSQLDateString(dob);

			int accountNumber = Integer.parseInt(account_no_string) + 1;
			insertMember(OFFICE_ID, accountNumber, firstname, lastname, displayName, gender_cv_id, dateOfBirth);

			BigDecimal clientId = getClientId(accountNumber);

			insertAddress(clientId, street);
			insertMemberIdentifier(clientId, panNo);
		}
		DBUtils.getInstance().commitTransaction();
	}

	public void insertMember(BigDecimal officeId, int accountNumber, String firstname, String lastname,
			String displayName, int genderCvv, String dateOfBirth) throws SQLException, ClassNotFoundException {
		String currentDate = DateUtils.getCurrentDateAsSqlDateString();
		String sql = "INSERT INTO m_client(office_id,account_no,activation_date,firstname,"
				+ "lastname,display_name,gender_cv_id,date_of_birth) VALUES('" + officeId + "'," + "'" + accountNumber
				+ "','" + currentDate + "','" + firstname + "','" + lastname + "','" + displayName + "','" + genderCvv
				+ "','" + dateOfBirth + "')";
		DBUtils.getInstance().executePreparedStatement(sql);

	}

	public BigDecimal getClientId(int accountNumber) throws ClassNotFoundException, SQLException {
		BigDecimal clientId = null;
		String sql = "select id from m_client where account_no=" + "'" + accountNumber + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			clientId = result.getBigDecimal(1);
		} else {
			System.out.println("No id for the client");
			throw new SQLException(
					"Client insertion has failed, no client ID got generated for Account Number:" + accountNumber);
		}
		System.out.println("Fetched client ID:" + clientId);
		return clientId;
	}

	public void insertAddress(BigDecimal clientId, String street) throws ClassNotFoundException, SQLException {


		String sql = "INSERT INTO m_client_address(client_id,address_id,\n" + "address_type_id,is_active) VALUES('"
				+ clientId + "'," + "'" + ADDRESSID + "','" + ADDRESSTYPEID + "','" + IS_ACTIVE + "')";
		DBUtils.getInstance().executePreparedStatement(sql);

		sql = "INSERT INTO  m_address(id,street) VALUES('" + clientId + "'," + "'" + street + "')";
		DBUtils.getInstance().executePreparedStatement(sql);
	}

	public void insertMemberIdentifier(BigDecimal clientId, String documentKey)
			throws ClassNotFoundException, SQLException {

		String sql = "INSERT INTO m_client_identifier(client_id,document_type_id,\n"
				+ "document_key,status,active) VALUES('" + clientId + "'," + "'" + DOCUMENT_TYPE_ID + "','"
				+ documentKey + "','" + STATUS + "','" + ACTIVE + "')";
		DBUtils.getInstance().executePreparedStatement(sql);
	}

}
