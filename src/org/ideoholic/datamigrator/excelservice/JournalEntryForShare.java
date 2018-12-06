package org.ideoholic.datamigrator.excelservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.ideoholic.datamigrator.utils.Constants;
import org.ideoholic.datamigrator.utils.DBUtils;
import org.ideoholic.datamigrator.utils.ExcelReaderUtils;

public class JournalEntryForShare implements Constants{
	private final ExcelReaderUtils excelReader;
	public JournalEntryForShare(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}
	public void importJournalEntryForShare(String inputValue2) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<ShareTransactionDataRetrieval> shareDataList = new ArrayList<ShareTransactionDataRetrieval>();
		shareDataList = getFromShareAccountTransaction();
		for (ShareTransactionDataRetrieval i : shareDataList) {
			int transaction_id = i.getShareId();
			int productId = getShareProductId(i.getShareId());
			int shareId = i.getShareAccountId();

			System.out.println("PRODUCT ID IS====" + productId);
			String S_transactionID = "SH" + transaction_id;
			System.out.println("s_transactionID    ID IS====" + S_transactionID);
			String transaction_date = i.getTransactionDate();
			BigDecimal amount = i.getShareAmount();

			String cdate = null;
			String cdate2 = null;
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date date = sdf.parse(transaction_date);
			System.out.println("DATEE ISSS==" + date);
			cdate = df.format(date);
			cdate2 = df.format(date);

			System.out.println("FINAL  DATEE ISSS==" + cdate);
			int office_id = 1;
			int reversed = 0;
			short manual_entry = 0;
			byte entity_type_enum = 4;
			int createdby_id = 1;
			int lastmodifiedby_id = 1;
			short is_running_balance_calculated = 0;
			BigDecimal office_running_balance = BigDecimal.ZERO;
			BigDecimal organization_running_balance = BigDecimal.ZERO;
			int m_share_product_mapping = 23;

	    	  insertintoaccgljournalentry(GL_JOURNAL_ACCOUNT_ID,office_id,CURRENCY_CODE,S_transactionID,
	  				reversed, manual_entry, transaction_date,2, amount,
	  				entity_type_enum, shareId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance,transaction_id);
	  		
	  		insertintoaccgljournalentry(m_share_product_mapping,office_id,CURRENCY_CODE,S_transactionID,
	  				reversed, manual_entry, transaction_date,1, amount,
	  				entity_type_enum, shareId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance,transaction_id);
	  		
	  		
	    	  insertintoaccgljournalentry(GL_JOURNAL_ACCOUNT_ID,office_id,CURRENCY_CODE,S_transactionID,
	  				reversed, manual_entry, transaction_date,1, amount,
	  				entity_type_enum, shareId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance,transaction_id);
	  		
	  		insertintoaccgljournalentry(m_share_product_mapping,office_id,CURRENCY_CODE,S_transactionID,
	  				reversed, manual_entry, transaction_date,2, amount,
	  				entity_type_enum, shareId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance,transaction_id);
		
		
		
		}  	
		
	//DBUtils.getInstance().commitTransaction();
	}
	public ArrayList<ShareTransactionDataRetrieval> getFromShareAccountTransaction() throws ClassNotFoundException, SQLException {
		ArrayList<ShareTransactionDataRetrieval> shareDataList = new ArrayList<ShareTransactionDataRetrieval>();
		String sql = "Select id,account_id, amount, transaction_date from m_share_account_transactions where status_enum ="+"'"+300+"'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			ShareTransactionDataRetrieval shareLoan = new ShareTransactionDataRetrieval();
			
			shareLoan.setShareId(result.getInt(1));
			shareLoan.setShareAccountId(result.getInt(2));
			shareLoan.setShareAmount(result.getBigDecimal(3));
			shareLoan.setTransactionDate(result.getString(4));
			shareDataList.add(shareLoan);
			
			System.out.println("+++++m_Loan_account_transaction TRansaction ID ****"+shareLoan.getShareId());
			System.out.println("+++++m_Loan_account_transaction Loan ID****"+shareLoan.getShareAccountId());
			System.out.println("+++++m_Loan_account_transaction Loan Amount****"+shareLoan.getShareAmount());
			System.out.println("+++++m_Loan_account_transaction Transaction Date****"+shareLoan.getTransactionDate());
			
		} else {
			System.out.println("JournalEntryForLoan.getClientId()::No id for the Client");
		}
		System.out.println("JournalEntryForLoan.getClientId()::Fetched no DATA:" +result.getInt(2));
		return shareDataList;
	}
	
	public int getShareProductId(int shareID) throws ClassNotFoundException, SQLException {
		int share_product_id = 0;
		String sql = "select product_id from m_share_account where id=" + "'" +shareID+ "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("JournalEntryForShare.getProductId()::Get Product Id Query=:" + sql);
		try {
		if (result.next()) {
			share_product_id = result.getInt(1);
		} 
	} catch (Exception e) {
		System.out.println("No share_product_id  for the Account ID"+e);
}

		System.out.println("Fetched share_product_id:" + share_product_id);
		return share_product_id;
	}
	
	private void insertintoaccgljournalentry(int glJournalAccountId, int office_id, String currencyCode,
			String S_transactionID, int reversed, short manual_entry, String transaction_date,
			int i, BigDecimal amount, byte entity_type_enum, int loanID, int createdby_id,
			int lastmodifiedby_id, String cdate, String cdate2, short is_running_balance_calculated,
			BigDecimal office_running_balance, BigDecimal organization_running_balance, int transaction_id)
			throws SQLException, ClassNotFoundException {

		
		String sql = "insert into acc_gl_journal_entry(account_id, office_id, currency_code, transaction_id,"
				+ "reversed,  manual_entry, entry_date,type_enum, amount,entity_type_enum,"
				+ " entity_id, createdby_id, lastmodifiedby_id,created_date, lastmodified_date, is_running_balance_calculated,"
				+ " office_running_balance,organization_running_balance,share_transaction_id)"
				+ "values('" + GL_JOURNAL_ACCOUNT_ID + "','" + office_id + "','" + CURRENCY_CODE + "','" + S_transactionID
				+ "','" + reversed + "','" + manual_entry + "','" + transaction_date + "','"
				+ i + "','" + amount + "','" + entity_type_enum + "','" + loanID + "','"
				+ createdby_id + "','" + lastmodifiedby_id + "','" + cdate + "','" + cdate2 + "','"
				+ is_running_balance_calculated + "','" + office_running_balance + "' ,'"+ organization_running_balance + "','"+transaction_id+"')";
		System.out.println("LoanJournalEntryImporter.insertaccgljournalentry()::sql for inserting in acc_gl_journal_entry = "+ sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}

}
