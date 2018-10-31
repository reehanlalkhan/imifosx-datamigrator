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
	
	public void importSavingsData(String inputValue, String inputDate) throws ParseException, ClassNotFoundException, SQLException {
		// This line will set account number start string to 0 if no number is passed

		Iterator<SavingsTransactionRow> excelIterator = excelReader.getWorkBookIteratorSavingsTransaction(0);
		while (excelIterator.hasNext()) {
			SavingsTransactionRow currentRow = excelIterator.next();
			String name = currentRow.getName();
			
			try {
			if(!name.isEmpty()) {
			
			int amount = currentRow.getAmount();
		//	amount
			//BigDecimal product_id_savings_normal = new BigDecimal(2);
			
			BigDecimal client_id = getClientId(name);

			if(client_id.compareTo(BigDecimal.ZERO) != 0) {
			int product_id;
			try {
				product_id = Integer.parseInt(inputValue);
			} catch (Exception ex) {
				System.out.println("SavingsAccountImporter.importSavingsAccount()::" + ex.getMessage());
				// In case of parse excepton assigning default product id
				product_id = DEFAULT_SAVINGS_PRODUCT_ID;
			}
			int savings_account_id = getSavingsAccountId(client_id,product_id);
			BigDecimal oldBalanceDerived = getAccountBalanceDerived(client_id,product_id);
			System.out.println("OLD BALANCE DERIVED"+oldBalanceDerived);
			short transaction_type_enum = 1;
			String transaction_date = inputDate;
			String balance_end_date_derived = DateUtils.getCurrentDateAsSqlDateString();
			int balance_number_of_days_derived =0;
			BigDecimal running_balance_derived = new BigDecimal(amount);
			BigDecimal cumulative_balance_derived = getCumulativeBalance(savings_account_id);
			BigDecimal cumulative_balance_derived_new = cumulative_balance_derived.add(new BigDecimal(amount));
		//	BigDecimal cumulative_balance_derived_new = new BigDecimal(0);
			String created_date = DateUtils.getCurrentDateAsSqlDateHMS();
			byte is_manual =0;
			BigDecimal account_balance_derived = oldBalanceDerived.add(new BigDecimal(amount));
			//			m_savings_account_transaction
			insertSavingAccountTransaction(savings_account_id,OFFICE_ID,transaction_type_enum,
					IS_REVERSED,transaction_date,amount,balance_end_date_derived,
					balance_number_of_days_derived,running_balance_derived,cumulative_balance_derived_new,created_date,APPUSER_ID,is_manual);
			updateSavingsAccount(account_balance_derived, savings_account_id);
		
		

			}else {
				System.out.println("Skipping the empty row");
			}
			}
			}
		 catch (Exception e) {
			System.out.println("Exception "+e);
		}
		}
		 DBUtils.getInstance().commitTransaction();
		}
	
	public int getSavingsAccountId(BigDecimal client_id,int product_id) throws ClassNotFoundException, SQLException {
		int savings_account_id = 0;
		String sql = "select id from m_savings_account where client_id=" + "'" + client_id + "'"
				+ "AND product_id=" + "'" + product_id + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("Get Savings account Id Query=:" + sql);

		try {
			if (result.next()) {
				savings_account_id = result.getInt(1);
			}
		} catch (Exception e) {
					System.out.println("No Savings Account id for the Client ID"+e);
		}
		
		System.out.println("Savings Account id " + savings_account_id);
		return savings_account_id;
	}
	public BigDecimal getAccountBalanceDerived(BigDecimal client_id,int product_id) throws ClassNotFoundException, SQLException {
		BigDecimal oldBalanceDerived = BigDecimal.ZERO;
		String sql = "select account_balance_derived from m_savings_account where client_id=" + "'" + client_id + "'"
				+ "AND product_id=" + "'" + product_id + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("Get oldBalanceDerived Query=:" + sql);

		try {
			if (result.next()) {
				oldBalanceDerived = result.getBigDecimal(1);
			}
		} catch (Exception e) {
					System.out.println("No oldBalanceDerived for the Client ID"+e);
		}
		System.out.println("Fetched oldBalanceDerived :" + oldBalanceDerived);
		return oldBalanceDerived;
	}

	public BigDecimal getClientId(String display_name) throws ClassNotFoundException, SQLException {
		BigDecimal clientId = BigDecimal.ZERO;
		String sql = "select id from m_client where display_name=" + "'" + display_name + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		try {
			if (result.next()) {
				clientId = result.getBigDecimal(1);
			}
		} catch (Exception e) {
					System.out.println("No id for the display name "+e);
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
	
	
	public BigDecimal getCumulativeBalance(int savings_account_id) throws ClassNotFoundException, SQLException {
		BigDecimal cumulative_balance_derived = BigDecimal.ZERO;
		String sql = "SELECT cumulative_balance_derived FROM `mifostenant-default`.m_savings_account_transaction where savings_account_id =" + "'" + savings_account_id + "'"
				+ "AND  transaction_type_enum=" + "'" + 1 + "'"+"ORDER BY cumulative_balance_derived DESC";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("Get cumulative_balance_derived Query=:" + sql);
		try {
			if (result.next()) {
				if(result.getBigDecimal(1)!=null) {
				cumulative_balance_derived = result.getBigDecimal(1);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception ************************* "+e);
		}
		
		System.out.println("Fetched cumulative_balance_derived:" + cumulative_balance_derived);
		return cumulative_balance_derived;
	}
	public void updateSavingsAccount(BigDecimal account_balance_derived, int savings_account_id) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE m_savings_account SET account_balance_derived =" + "'" + account_balance_derived +"'"+"WHERE id=" + "'"
				+ savings_account_id + "'";

		System.out.println("sql for updating m_savings_account= " + sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}
	
}
