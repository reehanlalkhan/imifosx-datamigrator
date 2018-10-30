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

public class SavingsDataImporter implements Constants{

	private final ExcelReaderUtils excelReader;

	public SavingsDataImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}
	
	public void importSavingsData() throws ParseException, ClassNotFoundException, SQLException {
		// This line will set account number start string to 0 if no number is passed

		Iterator<SavingsTransactionRow> excelIterator = excelReader.getWorkBookIteratorSavingsTransaction(0);
		while (excelIterator.hasNext()) {
			SavingsTransactionRow currentRow = excelIterator.next();
			String name = currentRow.getName();
			int amount = currentRow.getAmount();
		//	amount
			//BigDecimal product_id_savings_normal = new BigDecimal(2);
			
			BigDecimal client_id = getClientId(name);
			int savings_account_id = getSavingsAccountId(client_id);
			
			short transaction_type_enum = 1;
			String transaction_date = DateUtils.getCurrentDateAsSqlDateString();
			String balance_end_date_derived = DateUtils.getCurrentDateAsSqlDateString();
			int balance_number_of_days_derived =0;
			BigDecimal running_balance_derived = new BigDecimal(amount);
			double cumulative_balance_derived = 0.00000;
			BigDecimal cumulative_balance_derived_new = new BigDecimal(amount);
			String created_date = DateUtils.getCurrentDateAsSqlDateHMS();
			byte is_manual =0;
			
			//			m_savings_account_transaction
			insertSavingAccountTransaction(savings_account_id,OFFICE_ID,transaction_type_enum,
					IS_REVERSED,transaction_date,amount,balance_end_date_derived,
					balance_number_of_days_derived,running_balance_derived,cumulative_balance_derived_new,created_date,APPUSER_ID,is_manual);
		}
	  // DBUtils.getInstance().commitTransaction();
		
		}
	
	public int getSavingsAccountId(BigDecimal client_id) throws ClassNotFoundException, SQLException {
		int savings_account_id = 0;
		String sql = "select id from m_savings_account where client_id=" + "'" + client_id + "'"
				+ "AND product_id=" + "'" + 2 + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("Get Savings account Id Query=:" + sql);
		if (result.next()) {
			savings_account_id = result.getInt(1);
		} else {
			System.out.println("No Savings Account id for the Client ID");
			throw new SQLException(
					"Savings Transaction insertion has failed, no Savings Account id got generated for Client ID"
							+ client_id); 
		}
		System.out.println("Fetched Loan ID:" + savings_account_id);
		return savings_account_id;
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

	private void insertSavingAccountTransaction(int savings_account_id, BigDecimal officeId, short transaction_type_enum, byte isReversed, String transaction_date, int amount, String balance_end_date_derived, int balance_number_of_days_derived, BigDecimal running_balance_derived, BigDecimal cumulative_balance_derived_new, String created_date, BigDecimal appuserId, byte is_manual) throws SQLException, ClassNotFoundException {

		String sql = "INSERT into m_savings_account_transaction(savings_account_id,office_id,transaction_type_enum,\n" + 
				"					is_reversed,transaction_date,amount,balance_end_date_derived,\n" + 
				"					balance_number_of_days_derived,running_balance_derived,cumulative_balance_derived,created_date,"
				+ "appuser_id,is_manual)\n" + "values('" + savings_account_id + "','" + OFFICE_ID + "','"
				+ transaction_type_enum + "','" + IS_REVERSED + "','" + transaction_date + "','" + amount + "','"
				+ balance_end_date_derived + "','" + balance_number_of_days_derived + "','" + running_balance_derived
				+ "','" + cumulative_balance_derived_new + "','"+created_date+"','" + APPUSER_ID + "','"+is_manual+"')";
		System.out.println("sql for inserting in m_savings_account_transaction = " + sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}
	
}
