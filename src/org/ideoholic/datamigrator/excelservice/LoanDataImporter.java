package org.ideoholic.datamigrator.excelservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.ideoholic.datamigrator.utils.Constants;
import org.ideoholic.datamigrator.utils.DBUtils;
import org.ideoholic.datamigrator.utils.DateUtils;
import org.ideoholic.datamigrator.utils.ExcelReaderUtils;

public class LoanDataImporter implements Constants {

	private final ExcelReaderUtils excelReader;

	public LoanDataImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}

	public void importLoanData(String inputValue) throws ParseException, ClassNotFoundException, SQLException {
		// This line will set account number start string to 0 if no number is passed

		
		Iterator<LoanDataRow> excelIterator = excelReader.getWorkBookIteratorLoan(0);
		while (excelIterator.hasNext()) {
			LoanDataRow currentRow = excelIterator.next();
			int y = 18;
			String account_no = String.format("%09d", y);
			String display_name = currentRow.getDName();
			int product_id;
			try {
				product_id = Integer.parseInt(inputValue);
			} catch (Exception ex) {
				System.out.println("LoanDataImporter.importLoanData()::" + ex.getMessage());
				// In case of parse excepton assigning default product id
				product_id = DEFAULT_LOAN_PRODUCT_ID;
			}

			BigDecimal client_id = getClientId(display_name);
			Date disbursedDate = currentRow.getDisbursedDate();
			Date expiryDate = currentRow.getExpiryDate();
			double loanOS = currentRow.getLoanOS();
			String principal_amount_proposed = String.format("%.4f", loanOS);
			String principal_amount = String.format("%.4f", loanOS);
			String approved_principal = String.format("%.4f", loanOS);
			double nm = 0;
			String nominal_interest_rate_per_period = String.format("%.4f", nm);
			String annual_nominal_interest_rate = String.format("%.4f", nm);
			System.out.println("DD Check" + disbursedDate);
			String submittedon_date = DateUtils.getCurrentDateAsSqlDateString();
			System.out.println("DD Checked" + submittedon_date);
			String approvedon_date = DateUtils.getCurrentDateAsSqlDateString();
			String expected_disbursedon_date = DateUtils.getCurrentDateAsSqlDateString();
			String disbursedon_date = DateUtils.getCurrentDateAsSqlDateString();
			Date disbursedon1_date = DateUtils.getCurrentDateAsSqlDate();
			String expected_maturedon_date = DateUtils.getCurrentDateAsSqlDate(expiryDate);
			String maturedon_date = DateUtils.getCurrentDateAsSqlDate(expiryDate);
			String principal_disbursed_derived = String.format("%.4f", loanOS);
			String principal_outstanding_derived = String.format("%.4f", loanOS);

			insertLoan(account_no, client_id, product_id, LOAN_STATUS_ID, LOAN_TYPE_ENUM, CURRENCY_CODE,
					CURRENCY_DIGITS, CURRENCY_MULTIPLESOF, principal_amount_proposed, principal_amount,
					approved_principal, nominal_interest_rate_per_period, INTEREST_PERIOD_FREQUENCY_ENUM,
					annual_nominal_interest_rate, INTEREST_METHOD_ENUM, INTEREST_CALCULATED_IN_PERIOD_ENUM,
					TERM_FREQUENCY, TERM_PERIOD_FREQUENCY_ENUM, REPAY_EVERY, REPAYMENT_PERIOD_FREQUENCY_ENUM,
					NUMBER_OF_REPAYMENTS, AMORTIZATION_METHOD_ENUM, submittedon_date, approvedon_date,
					expected_disbursedon_date, disbursedon_date, DISBURSEDON_USERID, expected_maturedon_date,
					maturedon_date, principal_disbursed_derived, principal_outstanding_derived,
					LOAN_TRANSACTION_STRATEGY_ID, DAYS_IN_MONTH_ENUM, DAYS_IN_YEAR_ENUM, VERSION);

			DBUtils.getInstance().commitTransaction();

			BigDecimal loan_id = (BigDecimal) getLoanId(account_no);

			String transaction_date = DateUtils.getCurrentDateAsSqlDateString();
			String amount = String.format("%.4f", loanOS);
			String outstanding_loan_balance_derived = String.format("%.4f", loanOS);
			String submitted_on_date = DateUtils.getCurrentDateAsSqlDateString();
			String created_date = DateUtils.getCurrentDateAsSqlDateHMS();
			short transaction_type_enum = 1;
			short transaction_type_enum1 = 10;
			String amount1 = String.format("%.4f", nm);
			String outstanding_loan_balance_derived1 = String.format("%.4f", nm);

			insertLoanTransaction(loan_id, OFFICE_ID, IS_REVERSED, transaction_type_enum, transaction_date, amount,
					outstanding_loan_balance_derived, submitted_on_date, created_date, APPUSER_ID);
			DBUtils.getInstance().commitTransaction();
			insertLoanTransaction2(loan_id, OFFICE_ID, IS_REVERSED, transaction_type_enum1, transaction_date, amount1,
					outstanding_loan_balance_derived1, submitted_on_date, created_date, APPUSER_ID);
			DBUtils.getInstance().commitTransaction();
			String duedate = null;
			boolean completed_derived = false;
			String lastmodified_date = DateUtils.getCurrentDateAsSqlDateHMS();

			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(disbursedon1_date);
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(expiryDate);
			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
			System.out.println("No Of Months remaining: " + diffMonth);
			int amt_monthly_deduction = (int) (loanOS / diffMonth);
			System.out.println("Amount Paid Every Month " + amt_monthly_deduction);
			int new_loanOs;
			for (short j = 0; j < diffMonth; j++) {
				new_loanOs = (int) (loanOS - amt_monthly_deduction * j);
				System.out.println("NEWLOANOS" + new_loanOs);
				if (j != diffMonth - 1) {

					String dt = disbursedon_date; // Start date
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar d = Calendar.getInstance();
					Calendar c = Calendar.getInstance();
					d.setTime(sdf.parse(dt));
					c.setTime(sdf.parse(dt));
					d.add(Calendar.MONTH, j);
					c.add(Calendar.MONTH, j + 1); // number of days to add
					duedate = sdf.format(c.getTime());
					String duedate1 = sdf.format(d.getTime());
					short k = (short) (j + 1);
					insertRepaymentSchedule(loan_id, duedate1, duedate, k, amt_monthly_deduction, completed_derived,
							CREATEDBY_ID, created_date, lastmodified_date, LASTMODIFIEDBY_ID,
							RECALCULATED_INTEREST_COMPONENT);
					System.out.println("Monthly Payment  " + amt_monthly_deduction);
					DBUtils.getInstance().commitTransaction();

				} else {
					short p = (short) (j);
					// double final_payment = amt_monthly_deduction + new_loanOs;
					insertRepaymentSchedule1(loan_id, duedate, maturedon_date, p, new_loanOs, completed_derived,
							CREATEDBY_ID, created_date, lastmodified_date, LASTMODIFIEDBY_ID,
							RECALCULATED_INTEREST_COMPONENT);
					DBUtils.getInstance().commitTransaction();

				}

			}
		}
	}

	public void insertLoan(String account_no, BigDecimal client_id, int product_id, short loan_status_id,
			short loan_type_enum, String currency_code, short currency_digits, short currency_multiplesof,
			String principal_amount_proposed, String principal_amount, String approved_principal,
			String nominal_interest_rate_per_period, short interest_period_frequency_enum,
			String annual_nominal_interest_rate, short interest_method_enum, short interest_calculated_in_period_enum,
			short term_frequency, short term_period_frequency_enum, short repay_every,
			short repayment_period_frequency_enum, short number_of_repayments, short amortization_method_enum,
			String submittedon_date, String approvedon_date, String expected_disbursedon_date, String disbursedon_date,
			int disbursedon_userid, String expected_maturedon_date, String maturedon_date,
			String principal_disbursed_derived, String principal_outstanding_derived, int loan_transaction_strategy_id,
			short days_in_month_enum, short days_in_year_enum, int version)
			throws SQLException, ClassNotFoundException {
		// String currentDate = DateUtils.getCurrentDateAsSqlDateString();
		String sql = "INSERT INTO m_loan(account_no,client_id,product_id,loan_status_id,loan_type_enum,currency_code,currency_digits,currency_multiplesof,\n"
				+ "					principal_amount_proposed,principal_amount,approved_principal,nominal_interest_rate_per_period,interest_period_frequency_enum,\n"
				+ "					annual_nominal_interest_rate,interest_method_enum,\n"
				+ "					interest_calculated_in_period_enum,term_frequency,term_period_frequency_enum,repay_every,\n"
				+ "					repayment_period_frequency_enum,number_of_repayments,amortization_method_enum,submittedon_date,approvedon_date,expected_disbursedon_date,\n"
				+ "					disbursedon_date,disbursedon_userid,expected_maturedon_date,maturedon_date,principal_disbursed_derived,principal_outstanding_derived,\n"
				+ "					loan_transaction_strategy_id,days_in_month_enum,days_in_year_enum,version) VALUES('"
				+ account_no + "'," + "'" + client_id + "','" + product_id + "','" + loan_status_id + "','"
				+ loan_type_enum + "','" + currency_code + "','" + currency_digits + "','" + currency_multiplesof
				+ "','" + principal_amount_proposed + "','" + principal_amount + "','" + approved_principal + "','"
				+ nominal_interest_rate_per_period + "','" + interest_period_frequency_enum + "','"
				+ annual_nominal_interest_rate + "','" + interest_method_enum + "','"
				+ interest_calculated_in_period_enum + "','" + term_frequency + "','" + term_period_frequency_enum
				+ "','" + repay_every + "','" + repayment_period_frequency_enum + "'," + "'" + number_of_repayments
				+ "','" + amortization_method_enum + "','" + submittedon_date + "','" + approvedon_date + "','"
				+ expected_disbursedon_date + "','" + disbursedon_date + "','" + disbursedon_userid + "','"
				+ expected_maturedon_date + "','" + maturedon_date + "','" + principal_disbursed_derived + "','"
				+ principal_outstanding_derived + "','" + loan_transaction_strategy_id + "','" + days_in_month_enum
				+ "','" + days_in_year_enum + "','" + version + "')";
		DBUtils.getInstance().executePreparedStatement(sql);

	}

	public void insertLoanTransaction(BigDecimal loan_id, BigDecimal office_id, byte is_reversed,
			short transaction_type_enum, String transaction_date, String amount,
			String outstanding_loan_balance_derived, String submitted_on_date, String created_date,
			BigDecimal appuser_id) throws SQLException, ClassNotFoundException {
		String sql = "INSERT into m_loan_transaction(loan_id,office_id,is_reversed,transaction_type_enum,\n"
				+ "transaction_date,amount,outstanding_loan_balance_derived,\n"
				+ "submitted_on_date,created_date,appuser_id)\n" + "values('" + loan_id + "','" + office_id + "','"
				+ is_reversed + "','" + transaction_type_enum + "','" + transaction_date + "','" + amount + "','"
				+ outstanding_loan_balance_derived + "','" + submitted_on_date + "','" + created_date + "','"
				+ appuser_id + "')";
		DBUtils.getInstance().executePreparedStatement(sql);
	}

	public void insertLoanTransaction2(BigDecimal loan_id, BigDecimal office_id, byte is_reversed,
			short transaction_type_enum1, String transaction_date, String amount1,
			String outstanding_loan_balance_derived1, String submitted_on_date, String created_date,
			BigDecimal appuser_id) throws SQLException, ClassNotFoundException {
		String sql = "INSERT into m_loan_transaction(loan_id,office_id,is_reversed,transaction_type_enum,\n"
				+ "transaction_date,amount,outstanding_loan_balance_derived,\n"
				+ "submitted_on_date,created_date,appuser_id)\n" + "values('" + loan_id + "','" + office_id + "','"
				+ is_reversed + "','" + transaction_type_enum1 + "','" + transaction_date + "','" + amount1 + "','"
				+ outstanding_loan_balance_derived1 + "','" + submitted_on_date + "','" + created_date + "','"
				+ appuser_id + "')";
		DBUtils.getInstance().executePreparedStatement(sql);

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

	public BigDecimal getLoanId(String account_no) throws ClassNotFoundException, SQLException {
		BigDecimal loanId;
		String sql = "select id from m_loan where account_no=" + "'" + account_no + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			loanId = result.getBigDecimal(1);
		} else {
			System.out.println("No id for the Loan Account Number");
			throw new SQLException(
					"Loan Transaction insertion has failed, no Loan ID got generated for Loan Account Number"
							+ account_no);
		}
		System.out.println("Fetched Loan ID:" + loanId);
		return loanId;
	}

	public void insertRepaymentSchedule(BigDecimal loan_id, String duedate1, String duedate, short j,
			double amt_monthly_deduction, boolean completed_derived, BigInteger createdby_id, String created_date,
			String lastmodified_date, BigInteger lastmodifiedby_id, byte recalculated_interest_component)
			throws SQLException, ClassNotFoundException {
		String sql = "insert into m_loan_repayment_schedule(loan_id,fromdate,duedate,installment,principal_amount,\n"
				+ "completed_derived,createdby_id,created_date,\n"
				+ "lastmodified_date,lastmodifiedby_id,recalculated_interest_component) values ('" + loan_id + "','"
				+ duedate1 + "','" + duedate + "','" + j + "','" + amt_monthly_deduction + "'," + 0 + ",'"
				+ createdby_id + "','" + created_date + "','" + lastmodified_date + "','" + lastmodifiedby_id + "','"
				+ recalculated_interest_component + "')";
		System.out.println("sql" + sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}

	public void insertRepaymentSchedule1(BigDecimal loan_id, String duedate, String maturedon_date, short j,
			double new_loanOs, boolean completed_derived, BigInteger createdby_id, String created_date,
			String lastmodified_date, BigInteger lastmodifiedby_id, byte recalculated_interest_component)
			throws SQLException, ClassNotFoundException {
		String sql = "insert into m_loan_repayment_schedule(loan_id,fromdate,duedate,installment,principal_amount,\n"
				+ "completed_derived,createdby_id,created_date,\n"
				+ "lastmodified_date,lastmodifiedby_id,recalculated_interest_component) values ('" + loan_id + "','"
				+ duedate + "','" + maturedon_date + "','" + (j + 1) + "','" + new_loanOs + "'," + 0 + ",'"
				+ createdby_id + "','" + created_date + "','" + lastmodified_date + "','" + lastmodifiedby_id + "','"
				+ recalculated_interest_component + "')";
		System.out.println("sql1" + sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}

}
