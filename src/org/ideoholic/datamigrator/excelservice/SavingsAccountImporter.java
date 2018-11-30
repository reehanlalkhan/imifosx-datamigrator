package org.ideoholic.datamigrator.excelservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;

import org.ideoholic.datamigrator.utils.Constants;
import org.ideoholic.datamigrator.utils.DBUtils;
import org.ideoholic.datamigrator.utils.DateUtils;
import org.ideoholic.datamigrator.utils.ExcelReaderUtils;
import org.ideoholic.datamigrator.utils.MemberNameUtil;

public class SavingsAccountImporter implements Constants {

	private final ExcelReaderUtils excelReader;

	public SavingsAccountImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}

	public void importSavingsAccount(String inputValue2) throws ParseException, ClassNotFoundException, SQLException {
		// This line will set account number start string to 0 if no number is passed
/*
		Iterator<SavingsAccountRow> excelIterator = excelReader.getWorkBookIteratorSavingsAccount(0);
		while (excelIterator.hasNext()) {*/
	      int countofclients = getCountOfAllClientId();
	      // sql : select id from m_client where status_enum = 300
	      // while rs.next
	      // clientId = rs.getBigDecimal(1);
	      // rest of the logic
	      // while loop ends here
	      int i=1;
	     	String sql = "select id from m_client where status_enum = 300";
			ResultSet rs = DBUtils.getInstance().executeQueryStatement(sql);
	      
			while (rs.next()){
				i++;
		  BigDecimal clientId = rs.getBigDecimal(1);
/*	      System.out.println("COUNT OF CLIENTS--=="+countofclients);
	      for (int i=0; i<=countofclients;i++){
	    	  
			BigDecimal id_all=BigDecimal.ZERO;
			try {
				id_all = getAllClientId().add(new BigDecimal(i));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			*/
			
		/*	SavingsAccountRow currentRow = excelIterator.next();
			String name = currentRow.getName();*/
			BigDecimal amount = BigDecimal.ZERO;

/*			if(!name.isEmpty()) {
			if (amount.compareTo(BigDecimal.ZERO) == -1) {
				amount = amount.negate();
			}

			if (!MemberNameUtil.checkMemberName(name)) {
				continue;
			}
			BigDecimal clientId = getClientId(name);*/
			//BigDecimal clientId = id_all;
			System.out.println(
					"SavingsAccountImporter.importSavingsAccount()::SAVINGS ACCOUNT PRODUCT ID:=" + inputValue2);
			
			

			int product_id_savings;
			try {
				product_id_savings = Integer.parseInt(inputValue2);
			} catch (Exception ex) {
				System.out.println("SavingsAccountImporter.importSavingsAccount()::" + ex.getMessage());
				// In case of parse excepton assigning default product id
				product_id_savings = DEFAULT_SAVINGS_PRODUCT_ID;
			}
			
			int acc_id_check = getSavingsAccountId(clientId, product_id_savings);
			if(acc_id_check==0) {
		
			short status_enum = 300;
			short sub_status_enum = 0;
			short account_type_enum = 1;
			short deposit_type_enum = 100;

			short interest_compounding_period_enum = 1;
			short interest_calculation_type_enum = 1;

			short interest_posting_period_enum = 4;
			short interest_calculation_days_in_year_type_enum = 365;
			String submittedon_date = "2018-11-01";
			String approvedon_date = "2018-11-01";
			String activatedon_date = "2018-11-01";
			double nm = 0;
			int version = 1;
			int y = getCurrentMaxSavingsAccountId(0) + 1;
			String accountNumber = String.format("%09d", y);
		
			// String account_no = String.format("%09d", y);
			short transaction_type_enum = 1;
			String transaction_date = DateUtils.getCurrentDateAsSqlDateString();
			String balance_end_date_derived = DateUtils.getCurrentDateAsSqlDateString();
			int balance_number_of_days_derived = 0;
			BigDecimal cumulative_balance_derived_new = amount;
			BigDecimal running_balance_derived = amount;
			String created_date = DateUtils.getCurrentDateAsSqlDateHMS();
			byte is_manual = 0;
			BigDecimal account_balance_derived = amount;
			String nominal_annual_interest_rate = String.format("%.4f", nm);
			
			byte withdrawal_fee_for_transfer = 0 ; 
			int submittedon_userid =1;
			int approvedon_userid =1;
			int activatedon_userid =1;
			BigDecimal min_required_balance = BigDecimal.ZERO;
			BigDecimal overdraft_limit = BigDecimal.ZERO;
			String total_overdraft_interest_derived = null;
			insertSavingsAccount(accountNumber, clientId, product_id_savings, status_enum, sub_status_enum,
					account_type_enum, deposit_type_enum, submittedon_date,submittedon_userid, approvedon_date,approvedon_userid, activatedon_date,
					activatedon_userid,CURRENCY_CODE, CURRENCY_DIGITS,CURRENCY_MULTIPLESOF, nominal_annual_interest_rate, interest_compounding_period_enum,
					interest_posting_period_enum, interest_calculation_type_enum,
					interest_calculation_days_in_year_type_enum,withdrawal_fee_for_transfer,overdraft_limit,total_overdraft_interest_derived,account_balance_derived,min_required_balance, version);
			
/*			int savings_account_id = getSavingsAccountId(clientId, product_id_savings);
			insertSavingAccountTransaction(savings_account_id, OFFICE_ID, transaction_type_enum, IS_REVERSED,
					transaction_date, amount, balance_end_date_derived, balance_number_of_days_derived,
					running_balance_derived, cumulative_balance_derived_new, created_date, APPUSER_ID, is_manual);
			DBUtils.getInstance().commitTransaction();*/
			}
	      
			System.out.println("Skipping divident account Created for product id ==  "+product_id_savings);
			//if (i==5)break;
	      }
			DBUtils.getInstance().commitTransaction();
	}
/*		}
		}
	}*/

	public int getCurrentMaxSavingsAccountId(int accountNumber) throws ClassNotFoundException, SQLException {
		BigDecimal savingsId = null;
		String sql = "select max(account_no) from m_savings_account";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			savingsId = result.getBigDecimal(1);
		} else {
			System.out.println(
					"MemberDataImporter.getCurrentMaxClientId()::No max ID found from the client table, returning:"
							+ accountNumber);
			savingsId = new BigDecimal(accountNumber);
		}
		System.out.println(
				"SavingsAccountImporter.getCurrentMaxSavingsAccountId()::MemberDataImporter.getCurrentMaxClientId()::Fetched max client ID:"
						+ savingsId);
		if (savingsId == null) {
			savingsId = new BigDecimal(0);
		}

		return savingsId.intValue();
	}

	public BigDecimal getClientId(String display_name) throws ClassNotFoundException, SQLException {
		BigDecimal clientId = null;
		String sql = "select id from m_client where status_enum = 300 and display_name=" + "'" + display_name + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			clientId = result.getBigDecimal(1);
		} else {
			System.out.println("SavingsAccountImporter.getClientId()::No id for the display name");
			throw new SQLException(
					"Client insertion has failed, no client ID got generated for Display Name:" + display_name);
		}
		System.out.println("SavingsAccountImporter.getClientId()::Fetched client ID:" + clientId);
		return clientId;
	}
			public void insertSavingsAccount(String accountNumber, BigDecimal clientId, int product_id_savings,short status_enum, short sub_status_enum, short account_type_enum, short deposit_type_enum,String submittedon_date, int submittedon_userid, String approvedon_date, int approvedon_userid, String activatedon_date, int activatedon_userid, String currencyCode,short currencyDigits, short currencyMultiplesof, String nominal_annual_interest_rate, short interest_compounding_period_enum,short interest_posting_period_enum, short interest_calculation_type_enum,short interest_calculation_days_in_year_type_enum, byte withdrawal_fee_for_transfer,BigDecimal overdraft_limit,String total_overdraft_interest_derived, BigDecimal account_balance_derived, BigDecimal min_required_balance, int version)
			throws ClassNotFoundException, SQLException {
		String sql = "INSERT into m_savings_account(account_no,client_id,product_id,status_enum,sub_status_enum,account_type_enum,"
				+ "deposit_type_enum,submittedon_date,submittedon_userid,approvedon_date,approvedon_userid,activatedon_date,"
				+ "activatedon_userid,currency_code,currency_digits,currency_multiplesof,nominal_annual_interest_rate,"
				+ "interest_compounding_period_enum,interest_posting_period_enum,interest_calculation_type_enum,"
				+ "interest_calculation_days_in_year_type_enum,withdrawal_fee_for_transfer,overdraft_limit,total_overdraft_interest_derived,account_balance_derived,"
				+ "min_required_balance,version) VALUES('"
				+ accountNumber + "','" + clientId + "'," + "'" + product_id_savings + "','" + status_enum + "','"
				+ sub_status_enum + "','" + account_type_enum + "','" + deposit_type_enum + "','" + submittedon_date
				+ "','"+submittedon_userid+"','" + approvedon_date + "','"+approvedon_userid+"','" + activatedon_date + "','"+activatedon_userid+"','" + CURRENCY_CODE + "','" + CURRENCY_DIGITS
				+ "','"+CURRENCY_MULTIPLESOF+"','" + nominal_annual_interest_rate + "','" + interest_compounding_period_enum + "','"
				+ interest_posting_period_enum + "','" + interest_calculation_type_enum + "','"
				+ interest_calculation_days_in_year_type_enum + "','"+withdrawal_fee_for_transfer+"','"+overdraft_limit+"',"+total_overdraft_interest_derived+",'" + account_balance_derived + "','"+min_required_balance+"','" + version
				+ "')";
		DBUtils.getInstance().executePreparedStatement(sql);
		System.out.println("SavingsAccountImporter.insertSavingsAccount()::INSERT QUERY TO SAVINGS ACCOUNT" + sql);
	}

	/*private void insertSavingAccountTransaction(int savings_account_id, BigDecimal officeId,
			short transaction_type_enum, byte isReversed, String transaction_date, BigDecimal amount,
			String balance_end_date_derived, int balance_number_of_days_derived, BigDecimal running_balance_derived,
			BigDecimal cumulative_balance_derived_new, String created_date, BigDecimal appuserId, byte is_manual)
			throws SQLException, ClassNotFoundException {

		String sql = "INSERT into m_savings_account_transaction(savings_account_id,office_id,transaction_type_enum,\n"
				+ "					is_reversed,transaction_date,amount,balance_end_date_derived,\n"
				+ "					balance_number_of_days_derived,running_balance_derived,cumulative_balance_derived,created_date,"
				+ "appuser_id,is_manual)\n" + "values('" + savings_account_id + "','" + OFFICE_ID + "','"
				+ transaction_type_enum + "','" + IS_REVERSED + "','" + transaction_date + "','" + amount + "','"
				+ balance_end_date_derived + "','" + balance_number_of_days_derived + "','" + running_balance_derived
				+ "','" + cumulative_balance_derived_new + "','" + created_date + "','" + APPUSER_ID + "','" + is_manual
				+ "')";
		System.out.println(
				"SavingsAccountImporter.insertSavingAccountTransaction()::sql for inserting in m_savings_account_transaction = "
						+ sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}*/

	public int getSavingsAccountId(BigDecimal clientId, int productId) throws ClassNotFoundException, SQLException {
		int savings_account_id = 0;
		String sql = "select id from m_savings_account where client_id=" + "'" + clientId + "'" + "AND product_id="
				+ "'" + productId + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("SavingsAccountImporter.getSavingsAccountId()::Get Savings account Id Query=:" + sql);
		try {
		if (result.next()) {
			savings_account_id = result.getInt(1);
		} 
	} catch (Exception e) {
		System.out.println("No Savings Account id for the Client ID"+e);
}

		System.out.println("Fetched Savings ID:" + savings_account_id);
		return savings_account_id;
	}

	public BigDecimal getAllClientId() throws ClassNotFoundException, SQLException {
		BigDecimal clientId = null;
		String sql = "select id from m_client where status_enum = 300";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			clientId = result.getBigDecimal(1);
		} else {
			System.out.println("SavingsAccountImporter.getClientId()::No id for the display name");
			throw new SQLException(
					"Client insertion has failed, no client ID got generated for Display Name:");
		}
		System.out.println("SavingsAccountImporter.getClientId()::Fetched client ID:" + clientId);
		return clientId;
	}

	public int getCountOfAllClientId() throws ClassNotFoundException, SQLException {
		int countClientId = 0;
		String sql = "select COUNT(id) from m_client";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			countClientId = result.getInt(1);
		} else {
			System.out.println("SavingsAccountImporter.getClientId()::No id for the Client");
		}
		System.out.println("SavingsAccountImporter.getClientId()::Fetched no of clients:" + countClientId);
		return countClientId;
	}
	
	
}
