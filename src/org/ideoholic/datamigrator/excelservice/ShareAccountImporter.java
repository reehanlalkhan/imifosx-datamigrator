package org.ideoholic.datamigrator.excelservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ideoholic.datamigrator.utils.Constants;
import org.ideoholic.datamigrator.utils.DBUtils;
import org.ideoholic.datamigrator.utils.ExcelReaderUtils;

public class ShareAccountImporter implements Constants {
	private final ExcelReaderUtils excelReader;

	public ShareAccountImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}

	public void importShareData() throws ParseException, ClassNotFoundException, SQLException {
		// This line will set account number start string to 0 if no number is passed

		Iterator<ShareAccountRow> excelIterator = excelReader.getWorkBookIteratorShare(0);
		while (excelIterator.hasNext()) {
			ShareAccountRow currentRow = excelIterator.next();
			String display_name = currentRow.getDName();
			BigDecimal client_id =BigDecimal.ZERO;
		
			
			try {
				client_id = getClientId(display_name);
		
			}
			catch(Exception e) {
				//e.printStackTrace();
				System.out.println("EXception occurred no client id found"+display_name);
				continue;
			}
			int y = getCurrentMaxShareAccountId(0) + 1;
			String account_no = String.format("%09d", y);
			int product_id = 1;
				
			int total_approved_shares = currentRow.getNoOfShares();
			double unit_price = 100.00;
			double amount = unit_price * total_approved_shares;
			int status_enum = 300;
			String submitted_date = "2018-11-01";
			int submitted_userid = 1;
			String approved_date = "2018-11-01";
			int approved_userid = 1;
			String activated_date = "2018-11-01";
			int activated_userid = 1;
			String currency_code = "INR";
			short currency_digits = 2;
			short currency_multiplesof = 1;
			BigDecimal savings_account_id = new BigDecimal(getSavingsId(client_id));
			String transaction_date = "2018-11-01";
			int total_shares = total_approved_shares;
			BigInteger charge_amount = BigInteger.ZERO;
			double amount_paid = amount;
			short type_enum = 500;
			int shares = getCurrentMaxShares();
			int totalsubscribed_shares = shares + total_approved_shares;

			ArrayList<Integer> retrievedShareAccount = new ArrayList<Integer>();
			retrievedShareAccount = getfromshareaccountcertificate();
		//	System.out.println("Retrieved Data from Share Account is : " + retrievedShareAccount);
			short minimum_active_period_frequency_enum = 4;
			short lockin_period_frequency_enum = 4;
			
			int acc_id_check = getShareIdByClientId(client_id);
			int acc_id_check_type = getShareIdByClientIdForStatus(client_id);
			
		//	System.out.println("****acc_id_check==="+acc_id_check);
			//System.out.println("****acc_id_check_type==="+acc_id_check_type);
			
			if(acc_id_check==0 ||(acc_id_check!=0 && acc_id_check_type!=0)) {
			insertShareAccount(account_no, product_id, client_id, status_enum, total_approved_shares, submitted_date,
					submitted_userid, approved_date, approved_userid, activated_date, activated_userid, currency_code,
					currency_digits, currency_multiplesof, savings_account_id, minimum_active_period_frequency_enum,
					lockin_period_frequency_enum);
			DBUtils.getInstance().commitTransaction();

			BigDecimal share_account_id = getShareId(account_no);

			int share_transaction_id = retrievedShareAccount.get(0);
			int cert_num = retrievedShareAccount.get(1) + 1;
			int from_num = retrievedShareAccount.get(2) + 1;
			int to_num = from_num + total_approved_shares;
			// insertshareaccountcertificate(share_account_id,
			// share_transaction_id,cert_num, from_num, to_num);

			insertshareaccounttransaction(share_account_id, transaction_date, total_shares, unit_price, amount,
					charge_amount, amount_paid, status_enum, type_enum, IS_ACTIVE);
			DBUtils.getInstance().commitTransaction();
			updateshareproduct(totalsubscribed_shares, shares);
			DBUtils.getInstance().commitTransaction();
			}
			/*else if(acc_id_check==0 & acc_id_check_type==0) {
				insertShareAccount(account_no, product_id, client_id, status_enum, total_approved_shares, submitted_date,
						submitted_userid, approved_date, approved_userid, activated_date, activated_userid, currency_code,
						currency_digits, currency_multiplesof, savings_account_id, minimum_active_period_frequency_enum,
						lockin_period_frequency_enum);
				DBUtils.getInstance().commitTransaction();

				BigDecimal share_account_id = getShareId(account_no);

				int share_transaction_id = retrievedShareAccount.get(0);
				int cert_num = retrievedShareAccount.get(1) + 1;
				int from_num = retrievedShareAccount.get(2) + 1;
				int to_num = from_num + total_approved_shares;
				// insertshareaccountcertificate(share_account_id,
				// share_transaction_id,cert_num, from_num, to_num);

				insertshareaccounttransaction(share_account_id, transaction_date, total_shares, unit_price, amount,
						charge_amount, amount_paid, status_enum, type_enum, IS_ACTIVE);
				DBUtils.getInstance().commitTransaction();
				updateshareproduct(totalsubscribed_shares, shares);
				DBUtils.getInstance().commitTransaction();
			
			}*/
			else {
				System.out.println("NO SHARE ACCOUNT CREATED"+display_name);
			}
				
		}
		}
	

	private void updateshareproduct(int totalsubscribed_shares, int shares) {
		String sql = "UPDATE m_share_product SET totalsubscribed_shares = " + "'" + totalsubscribed_shares + "'"
				+ " WHERE totalsubscribed_shares =" + "'" + shares + "'";
		try {
			DBUtils.getInstance().executePreparedStatement(sql);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void insertshareaccounttransaction(BigDecimal share_account_id, String transaction_date, int total_shares,
			double unit_price, double amount, BigInteger charge_amount, double amount_paid, int status_enum,
			short type_enum, byte is_active) throws SQLException, ClassNotFoundException {
		String sql = "insert into m_share_account_transactions"
				+ "(account_id, transaction_date, total_shares, unit_price, amount, charge_amount, amount_paid, status_enum, type_enum, is_active) "
				+ "VALUES('" + share_account_id + "','" + transaction_date + "','" + total_shares + "','" + unit_price
				+ "','" + amount + "'," + "'" + charge_amount + "','" + amount_paid + "','" + status_enum + "','"
				+ type_enum + "','" + is_active + "')";

		DBUtils.getInstance().executePreparedStatement(sql);
	}

	private void insertshareaccountcertificate(BigDecimal share_account_id, int share_transaction_id, int cert_num,
			int from_num, int to_num) throws SQLException, ClassNotFoundException {
		String sql = "insert into m_share_account_certificate\n"
				+ "(share_account_id, share_transaction_id,cert_num, from_num, to_num) values ('" + share_account_id
				+ "'," + "'" + share_transaction_id + "','" + cert_num + "','" + from_num + "','" + to_num + "')";
	//	System.out.println("sql for insertion in share account  certificate  = : " + sql);
		DBUtils.getInstance().executePreparedStatement(sql);
	}

	private void insertShareAccount(String account_no, int product_id, BigDecimal client_id, int status_enum,
			int total_approved_shares, String submitted_date, int submitted_userid, String approved_date,
			int approved_userid, String activated_date, int activated_userid, String currency_code,
			short currency_digits, short currency_multiplesof, BigDecimal savings_account_id,
			short minimum_active_period_frequency_enum, short lockin_period_frequency_enum)
			throws SQLException, ClassNotFoundException {
		String sql = "insert into m_share_account(account_no,product_id, client_id,  status_enum,"
				+ " total_approved_shares, submitted_date, submitted_userid, approved_date, approved_userid, activated_date, "
				+ "activated_userid,currency_code, currency_digits, currency_multiplesof, savings_account_id, "
				+ "minimum_active_period_frequency_enum, lockin_period_frequency_enum)values('" + account_no + "','"
				+ product_id + "'," + "'" + client_id + "','" + status_enum + "','" + total_approved_shares + "','"
				+ submitted_date + "','" + submitted_userid + "'," + "'" + approved_date + "','" + approved_userid
				+ "','" + activated_date + "','" + activated_userid + "','" + currency_code + "'," + "'"
				+ currency_digits + "','" + currency_multiplesof + "','" + savings_account_id + "'," + "'"
				+ minimum_active_period_frequency_enum + "','" + lockin_period_frequency_enum + "')";
	//	System.out.println("sql for insertion in share account = : " + sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}

	public BigDecimal getClientId(String display_name) throws ClassNotFoundException, SQLException {
		BigDecimal clientId = null;
		String sql = "select id from m_client where status_enum = 300 and display_name=" + "'" + display_name + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			clientId = result.getBigDecimal(1);
		} else {
		//	System.out.println("No id for the display name");
			throw new SQLException(
					"Client insertion has failed, no client ID got generated for Display Name:" + display_name);
		}
	//	System.out.println("Fetched client ID:" + clientId);
		return clientId;
	}

	public ArrayList<Integer> getfromshareaccountcertificate() throws ClassNotFoundException, SQLException {
		ArrayList<Integer> shareAccount = new ArrayList<Integer>();
		String sql = "select max(share_transaction_id),max(cert_num), max(to_num) from m_share_account_certificate";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			shareAccount.add(result.getInt(1));
			shareAccount.add(result.getInt(2));
			shareAccount.add(result.getInt(3));
		} else {
	//		System.out.println("No share_transaction_id,cert_num");
			throw new SQLException("No share_transaction_id,cert_num");
		}
	//	System.out.println("Fetched array list--:" + shareAccount);
		return shareAccount;
	}

	public int getCurrentMaxShareAccountId(int accountNumber) throws ClassNotFoundException, SQLException {
		BigDecimal shareId = null;
		String sql = "select max(account_no) from m_share_account";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			shareId = result.getBigDecimal(1);
		} else {
		//	System.out.println(
		//			"MemberDataImporter.getCurrentMaxClientId()::No max ID found from the client table, returning:"
		//					+ accountNumber);
			shareId = new BigDecimal(accountNumber);
		}
	//	System.out.println(
			//	"ShareAccountImporter.ShareAccountId()::MemberDataImporter.getCurrentMaxClientId()::Fetched max client ID:"
		//				+ shareId);
		if (shareId == null) {
			shareId = new BigDecimal(0);
		}
		return shareId.intValue();
	}

	public int getCurrentMaxShares() throws ClassNotFoundException, SQLException {
		BigDecimal shares = null;
		String sql = "select max(totalsubscribed_shares) from m_share_product";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			shares = result.getBigDecimal(1);
		} else {
		//	System.out.println("NO SHARES FOUND");
		}
		//System.out.println("No of shares = :" + shares);
		if (shares == null) {
			shares = new BigDecimal(0);
		}
		return shares.intValue();
	}

	public int getSavingsId(BigDecimal client_id) throws ClassNotFoundException, SQLException {
		int SavingsId = 0;
		String sql = "select id from m_savings_account where product_id = 1 and client_id=" + "'" + client_id + "'"
				+ "AND status_enum=300";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			SavingsId = result.getInt(1);
		} else {
			//System.out.println("No savings id for the client id" + client_id);
			throw new SQLException("no savings ID got generated for client id::" + client_id);
		}
		//System.out.println("Fetched savings ID :" + SavingsId);
		return SavingsId;
	}

	public BigDecimal getShareId(String account_no) throws ClassNotFoundException, SQLException {
		BigDecimal shareId=BigDecimal.ZERO;
		String sql = "select id from m_share_account where account_no=" + "'" + account_no + "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			shareId = result.getBigDecimal(1);
		} else {
		//	System.out.println("No share id for the share Account Number" + account_no);
			throw new SQLException("no Share ID got generated for  Share Account Number" + account_no);
		}
		//System.out.println("Fetched Share ID:" + shareId);
		return shareId;
	}
	
	public int getShareIdByClientId(BigDecimal client_id) throws ClassNotFoundException, SQLException {
		int shareId=0;
		String sql = "select id from m_share_account where product_id = 1 and client_id=" + "'" + client_id + "'" + "AND status_enum=300";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		try {
			if (result.next()) {
				shareId = result.getInt(1);
			} 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//System.out.println("No share id for the share Account Number" + e1);
			throw new SQLException("no Share ID got generated for  Share Account Number" + e1);
		}	//System.out.println("Fetched Share ID:" + shareId);
		return shareId;

	}
	
	public int getShareIdByClientIdForStatus(BigDecimal client_id) throws ClassNotFoundException, SQLException {
		int shareId=0;
		String sql = "select id from m_share_account where product_id = 1 and client_id=" + "'" + client_id + "'" + "AND status_enum=500";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		try {
			if (result.next()) {
				shareId = result.getInt(1);
			} 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//System.out.println("No share id for the share Account Number" + e1);
			throw new SQLException("no Share ID got generated for  Share Account Number" + e1);
		}	//System.out.println("Fetched Share ID:" + shareId);
		return shareId;

	}

}