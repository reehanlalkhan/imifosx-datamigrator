package org.ideoholic.datamigrator.excelservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
//import java.util.Date;
import java.util.Iterator;

import org.ideoholic.datamigrator.utils.DBUtils;
import org.ideoholic.datamigrator.utils.DateUtils;
import org.ideoholic.datamigrator.utils.ExcelReaderUtils;


public class LoanDataImporter {
	
	private final ExcelReaderUtils excelReader;
	
	public LoanDataImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}


	public void importLoanData() throws ParseException, ClassNotFoundException, SQLException {
		// This line will set account number start string to 0 if no number is passed
		
		Iterator<LoanDataRow> excelIterator = excelReader.getWorkBookIteratorLoan(0);
		while (excelIterator.hasNext()) {
			LoanDataRow currentRow = excelIterator.next();
			int account_no= 11;
			BigDecimal client_id = new BigDecimal(33);
			Date disbursedDate= currentRow.getDisbursedDate();
			Date expiryDate = currentRow.getExpiryDate();
			double loanOS = currentRow.getLoanOS();
			BigInteger product_id = BigInteger.valueOf(1);
			short loan_status_id = 300;
			short loan_type_enum = 1;
			String currency_code = "INR";
			short currency_digits = 2;
			short currency_multiplesof =1;
			String principal_amount_proposed = String.format("%.4f", loanOS);
			String principal_amount = String.format("%.4f", loanOS);
			String approved_principal = String.format("%.4f", loanOS);
			double nm = 0;
			String nominal_interest_rate_per_period = String.format("%.4f", nm);
			short interest_period_frequency_enum=2;
			String annual_nominal_interest_rate = String.format("%.4f", nm);
			short interest_method_enum = 0;
			short interest_calculated_in_period_enum=1;
			short term_frequency =12;
			short term_period_frequency_enum=2;
			short repay_every=1;
			short repayment_period_frequency_enum=2;
			short number_of_repayments =12;
			short amortization_method_enum=1;
			System.out.println("DD Check"+disbursedDate);
			String submittedon_date = DateUtils.getCurrentDateAsSqlDate(disbursedDate);
			System.out.println("DD Checked"+submittedon_date);
			String approvedon_date = DateUtils.getCurrentDateAsSqlDate(disbursedDate);
			String expected_disbursedon_date =  DateUtils.getCurrentDateAsSqlDate(disbursedDate);
			String disbursedon_date = DateUtils.getCurrentDateAsSqlDate(disbursedDate);
			int disbursedon_userid =1;
			String expected_maturedon_date=DateUtils.getCurrentDateAsSqlDate(expiryDate);
			String maturedon_date=DateUtils.getCurrentDateAsSqlDate(expiryDate);
			String principal_disbursed_derived = String.format("%.4f", loanOS);
			String principal_outstanding_derived = String.format("%.4f", loanOS);
		//	String total_expected_repayment_derived = String.format("%.4f", loanOS);
		//	String total_outstanding_derived = String.format("%.4f", loanOS);
			int loan_transaction_strategy_id =1;
			short days_in_month_enum =1;
			short days_in_year_enum =1;
			int version =3;
			
			insertLoan(account_no,client_id,product_id,loan_status_id,loan_type_enum,currency_code,currency_digits,currency_multiplesof,
					principal_amount_proposed,principal_amount,approved_principal,nominal_interest_rate_per_period,interest_period_frequency_enum,
					annual_nominal_interest_rate,interest_method_enum,
					interest_calculated_in_period_enum,term_frequency,term_period_frequency_enum,repay_every,
					repayment_period_frequency_enum,number_of_repayments,amortization_method_enum,submittedon_date,approvedon_date,expected_disbursedon_date,
					disbursedon_date,disbursedon_userid,expected_maturedon_date,maturedon_date,principal_disbursed_derived,principal_outstanding_derived,
					loan_transaction_strategy_id,days_in_month_enum,days_in_year_enum,version);
		}
		DBUtils.getInstance().commitTransaction();
	}

	
	public void insertLoan(int account_no, BigDecimal client_id, BigInteger product_id, short loan_status_id, short loan_type_enum, String currency_code, short currency_digits, short currency_multiplesof, String principal_amount_proposed, String principal_amount, String approved_principal, String nominal_interest_rate_per_period, short interest_period_frequency_enum, String annual_nominal_interest_rate, short interest_method_enum, short interest_calculated_in_period_enum, short term_frequency, short term_period_frequency_enum, short repay_every, short repayment_period_frequency_enum, short number_of_repayments, short amortization_method_enum, String submittedon_date, String approvedon_date, String expected_disbursedon_date, String disbursedon_date, int disbursedon_userid, String expected_maturedon_date, String maturedon_date, String principal_disbursed_derived, String principal_outstanding_derived, int loan_transaction_strategy_id, short days_in_month_enum, short days_in_year_enum, int version) throws SQLException, ClassNotFoundException {
		//String currentDate = DateUtils.getCurrentDateAsSqlDateString();
		String sql = "INSERT INTO m_loan(account_no,client_id,product_id,loan_status_id,loan_type_enum,currency_code,currency_digits,currency_multiplesof,\n" + 
				"					principal_amount_proposed,principal_amount,approved_principal,nominal_interest_rate_per_period,interest_period_frequency_enum,\n" + 
				"					annual_nominal_interest_rate,interest_method_enum,\n" + 
				"					interest_calculated_in_period_enum,term_frequency,term_period_frequency_enum,repay_every,\n" + 
				"					repayment_period_frequency_enum,number_of_repayments,amortization_method_enum,submittedon_date,approvedon_date,expected_disbursedon_date,\n" + 
				"					disbursedon_date,disbursedon_userid,expected_maturedon_date,maturedon_date,principal_disbursed_derived,principal_outstanding_derived,\n" + 
				"					loan_transaction_strategy_id,days_in_month_enum,days_in_year_enum,version) VALUES('" + account_no + "'," + "'" + client_id
				+ "','" + product_id + "','" + loan_status_id + "','" + loan_type_enum + "','" + currency_code + "','" + currency_digits
				+ "','" + currency_multiplesof + "','"+principal_amount_proposed+"','"+principal_amount+"','"+approved_principal+"','"+nominal_interest_rate_per_period+"','"+interest_period_frequency_enum+"','"+annual_nominal_interest_rate+"','"+interest_method_enum+"','"+interest_calculated_in_period_enum+"','"+term_frequency+"','"+term_period_frequency_enum+"','"+repay_every+"','"+repayment_period_frequency_enum+"',"
						+ "'"+number_of_repayments+"','"+amortization_method_enum+"','"+submittedon_date+"','"+approvedon_date+"','"+expected_disbursedon_date+"','"+disbursedon_date+"','"+disbursedon_userid+"','"+expected_maturedon_date+"','"+maturedon_date+"','"+principal_disbursed_derived+"','"+principal_outstanding_derived+"','"+loan_transaction_strategy_id+"','"+days_in_month_enum+"','"+days_in_year_enum+"','"+version+"')";
		DBUtils.getInstance().executePreparedStatement(sql);

	}
	
	public BigDecimal getClientId(int account_no) throws ClassNotFoundException, SQLException {
		BigDecimal clientId = null;
		String sql = "select id from m_client where account_no=" + "'" + account_no + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			clientId = result.getBigDecimal(1);
		} else {
			System.out.println("No id for the client");
			throw new SQLException(
					"Client insertion has failed, no client ID got generated for Account Number:" + account_no);
		}
		System.out.println("Fetched client ID:" + clientId);
		return clientId;
	}
}
