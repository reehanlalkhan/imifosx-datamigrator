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
import org.ideoholic.datamigrator.utils.Pair;

public class LoanTransactionImporter implements Constants {

	private final ExcelReaderUtils excelReader;

	public LoanTransactionImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}

	public void importTransactionData() throws ParseException, ClassNotFoundException, SQLException {
		Iterator<LoanTransactionRow> excelIterator = excelReader.getWorkBookIteratorLoanTransaction(0);
		while (excelIterator.hasNext()) {
			LoanTransactionRow currentRow = excelIterator.next();
			String name = currentRow.getName();
			int amount = currentRow.getAmount();
			System.out.println("name==: " + name);
			System.out.println("amount==: " + amount);
			BigDecimal client_id = getClientId(name);
			System.out.println("client==: " + client_id);
			BigDecimal loan_id = (BigDecimal) getLoanId(client_id);
			System.out.println("loan==: " + loan_id);
			Pair<BigDecimal, BigDecimal> repaidAndOutstanding = getPrincipalRepaidAndOutstandingDerived(loan_id);
			BigDecimal principal_repaid_derived =repaidAndOutstanding.first;
			BigDecimal principal_outstanding_derived = repaidAndOutstanding.second;
			int version = getVersion(loan_id);
			System.out.println("prd" + principal_repaid_derived);
			System.out.println("pod" + principal_outstanding_derived);
			System.out.println("ver" + version);
			int repay_id = getRepayId(loan_id);
			System.out.println("repay_id==" + repay_id);

			BigDecimal principal_repaid_derived_new = principal_repaid_derived.add(new BigDecimal(amount));
			BigDecimal principal_outstanding_derived_new = principal_outstanding_derived
					.subtract(new BigDecimal(amount));
			int version_new = version + 1;
			System.out.println("principal_repaid_derived_new==" + principal_repaid_derived_new);
			System.out.println("principal_outstanding_derived_new==" + principal_outstanding_derived_new);
			System.out.println("version_new==" + version_new);
			BigDecimal total_repayment_derived = principal_repaid_derived.add(new BigDecimal(amount));
			BigDecimal total_outstanding_derived = principal_outstanding_derived.subtract(new BigDecimal(amount));


			String transaction_date = DateUtils.getCurrentDateAsSqlDateString();
			BigDecimal amount_new = new BigDecimal(amount);
			BigDecimal principal_portion_derived = new BigDecimal(amount);
			String outstanding_loan_balance_derived = String.format("%.4f", principal_outstanding_derived_new);
			String submitted_on_date = DateUtils.getCurrentDateAsSqlDateString();
			String created_date = DateUtils.getCurrentDateAsSqlDateHMS();
			short transaction_type_enum = 2;


			BigDecimal principal_completed_derived = new BigDecimal(amount);
			boolean completed_derived = false;
			String obligations_met_on_date = DateUtils.getCurrentDateAsSqlDateString();
			String lastmodified_date = DateUtils.getCurrentDateAsSqlDateHMS();



			System.out.println("First   =  "+repaidAndOutstanding.first);
			System.out.println("Second  =  "+repaidAndOutstanding.second);
			
			updateLoan(principal_repaid_derived_new, principal_outstanding_derived_new, total_repayment_derived,
					total_outstanding_derived, version_new, loan_id);

			insertLoanTransaction(loan_id, OFFICE_ID, IS_REVERSED, transaction_type_enum, transaction_date, amount_new,
					principal_portion_derived, outstanding_loan_balance_derived, submitted_on_date, created_date,
					APPUSER_ID);

			updateRepaymentSchedule(principal_completed_derived, completed_derived, obligations_met_on_date,
					lastmodified_date, repay_id);

		}
		 DBUtils.getInstance().commitTransaction();
	}

	public BigDecimal getClientId(String display_name) throws ClassNotFoundException, SQLException {
		BigDecimal clientId = null;
		String sql = "select id from m_client where display_name=" + "'" + display_name + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			clientId = result.getBigDecimal(1);
		} else {
			System.out.println("No id for the display name");
			throw new SQLException(
					"Client insertion has failed, no client ID got generated for Display Name:" + display_name);
		}
		System.out.println("Fetched client ID:" + clientId);
		return clientId;
	}

	public BigDecimal getLoanId(BigDecimal client_id) throws ClassNotFoundException, SQLException {
		BigDecimal loanId;
		String sql = "select id from m_loan where client_id=" + "'" + client_id + "'" + "AND loan_status_id=" + "'"
				+ 300 + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			loanId = result.getBigDecimal(1);
		} else {
			System.out.println("No id for the Loan Account Number");
			throw new SQLException(
					"Loan Transaction insertion has failed, no Loan ID got generated for Loan Account Number"
							+ client_id);
		}
		System.out.println("Fetched Loan ID:" + loanId);
		return loanId;
	}

	/*public BigDecimal getPrincipalRepaidDerived(BigDecimal loan_id) throws ClassNotFoundException, SQLException {
		BigDecimal principal_repaid_derived;
		String sql = "select principal_repaid_derived from m_loan where id=" + "'" + loan_id + "'";
		// +"AND where loan_status_id="+"'"+300+"'"
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			principal_repaid_derived = result.getBigDecimal(1);
		} else {
			System.out.println("No principal_repaid_derived for the Loan Id");
			throw new SQLException(
					"Loan Transaction insertion has failed, no principal_repaid_derived got generated for Loan Id"
							+ loan_id);
		}
		System.out.println("principal_repaid_derived:" + principal_repaid_derived);
		return principal_repaid_derived;
	}

	

	public BigDecimal getPrincipalOutstandingDerived(BigDecimal loan_id) throws ClassNotFoundException, SQLException {
		BigDecimal principal_outstanding_derived;
		String sql = "select principal_outstanding_derived from m_loan where id=" + "'" + loan_id + "'";
		// +"AND where loan_status_id="+"'"+300+"'"
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			principal_outstanding_derived = result.getBigDecimal(1);
		} else {
			System.out.println("No principal_outstanding_derived for the Loan ID");
			throw new SQLException(
					"Loan Transaction insertion has failed, no principal_outstanding_derived got generated for Loan Id"
							+ loan_id);
		}
		System.out.println("Fetched principal_outstanding_derived:" + principal_outstanding_derived);
		return principal_outstanding_derived;
	}*/

	private Pair<BigDecimal, BigDecimal> getPrincipalRepaidAndOutstandingDerived(BigDecimal loan_id)throws ClassNotFoundException, SQLException {
		BigDecimal principal_repaid_derived, principal_outstanding_derived = null;
		String sql = "select principal_repaid_derived, principal_outstanding_derived from m_loan where id=" + "'" + loan_id + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			principal_repaid_derived = result.getBigDecimal(1);
			principal_outstanding_derived = result.getBigDecimal(2);
			
		} else {
			System.out.println("No principal_repaid_derived for the Loan Id");
			throw new SQLException(
					"Loan Transaction insertion has failed, no principal_repaid_derived got generated for Loan Id"
							+ loan_id);
		}
		System.out.println("Fetched Loan ID:" + principal_repaid_derived);
		
		return Pair.of(principal_repaid_derived, principal_outstanding_derived);
	}
	
	
	
	public int getVersion(BigDecimal loan_id) throws ClassNotFoundException, SQLException {
		int version = 0;
		String sql = "select version from m_loan where id=" + "'" + loan_id + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			version = result.getInt(1);
		} else {
			System.out.println("No version for the Loan Id");
			throw new SQLException(
					"Loan Transaction insertion has failed, no version got generated for Loan Id" + loan_id);
		}
		System.out.println("Fetched version:" + version);
		return version;
	}

	public int getRepayId(BigDecimal loan_id) throws ClassNotFoundException, SQLException {
		int repay_id = 0;
		String sql = "select id from m_loan_repayment_schedule where loan_id=" + "'" + loan_id + "'"
				+ "AND completed_derived=" + "'" + 0 + "'" + "ORDER BY installment";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("Get Repay ID Query=:" + sql);
		if (result.next()) {
			repay_id = result.getInt(1);
		} else {
			System.out.println("No id for the Loan Account Number");
			throw new SQLException(
					"Loan Transaction insertion has failed, no Loan ID got generated for Loan Account Number"
							+ loan_id);
		}
		System.out.println("Fetched Loan ID:" + repay_id);
		return repay_id;
	}

	public void updateLoan(BigDecimal principal_repaid_derived_new, BigDecimal principal_outstanding_derived_new,
			BigDecimal total_repayment_derived, BigDecimal total_outstanding_derived, int version_new,
			BigDecimal loan_id) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE m_loan SET principal_repaid_derived =" + "'" + principal_repaid_derived_new + "'"
				+ ", principal_outstanding_derived=" + "'" + principal_outstanding_derived_new + "'"
				+ ", total_repayment_derived=" + "'" + total_repayment_derived + "'" + ", total_outstanding_derived="
				+ "'" + total_outstanding_derived + "'" + ", version=" + "'" + version_new + "'" + "WHERE id=" + "'"
				+ loan_id + "'";

		System.out.println("sql for updating m_loan= " + sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}

	private void insertLoanTransaction(BigDecimal loan_id, BigDecimal office_id, byte is_reversed,
			short transaction_type_enum, String transaction_date, BigDecimal amount_new,
			BigDecimal principal_portion_derived, String outstanding_loan_balance_derived, String submitted_on_date,
			String created_date, BigDecimal appuser_id) throws SQLException, ClassNotFoundException {

		String sql = "INSERT into m_loan_transaction(loan_id,office_id,is_reversed,transaction_type_enum,"
				+ "transaction_date,amount,principal_portion_derived,outstanding_loan_balance_derived,"
				+ "submitted_on_date,created_date,appuser_id)\n" + "values('" + loan_id + "','" + office_id + "','"
				+ is_reversed + "','" + transaction_type_enum + "','" + transaction_date + "','" + amount_new + "','"
				+ principal_portion_derived + "','" + outstanding_loan_balance_derived + "','" + submitted_on_date
				+ "','" + created_date + "','" + appuser_id + "')";
		System.out.println("sql for inserting loan transaction= " + sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}

	public void updateRepaymentSchedule(BigDecimal principal_completed_derived, boolean completed_derived,
			String obligations_met_on_date, String lastmodified_date, int repay_id)
			throws SQLException, ClassNotFoundException {
		String sql = "UPDATE m_loan_repayment_schedule SET principal_completed_derived =" + "'"
				+ principal_completed_derived + "'" + ", completed_derived=" + "" + 1 + ""
				+ ", obligations_met_on_date=" + "'" + obligations_met_on_date + "'" + ", lastmodified_date=" + "'"
				+ lastmodified_date + "'" + "WHERE id=" + "'" + repay_id + "'";

		System.out.println("sql for updating Repayment Schedule= " + sql);
		DBUtils.getInstance().executePreparedStatement(sql);
	}

}
